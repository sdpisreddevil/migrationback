
package com.datamigration.scripts;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.datamigration.dbconnection.MSAccessConnect;
import com.datamigration.dbconnection.MysqlConnect;
import com.datamigration.dbtabledata.TableColumnMetaExtractor;

public class Utils {

    private String tblName;
    private String colNames = "";
    private String sqlQuery = "";
    private TableColumnMetaExtractor tblMetaExtract;
    private Connection con;
    private DBMessage dbMsg;
    private MSAccessConnect msAccessConnect;
    private MysqlConnect mysqlConnect;
    public Utils() {}

    public Utils(String tblName) {
        msAccessConnect = new MSAccessConnect();
        mysqlConnect = new MysqlConnect();
        this.tblName = tblName;
        tblMetaExtract = new TableColumnMetaExtractor();
        colNames = tblMetaExtract.getColumnMetaData(tblName);
    }

    public int checkVarchar(String tempColName) {
        int flag = 0;
        dbMsg = msAccessConnect.getCurrentMsaccessConnection();
        if(dbMsg.getCODE() != 0){
            return flag;
        }
        con = dbMsg.getDbCon();
        ResultSet rsColumns = null;
        try {
            DatabaseMetaData meta = con.getMetaData();
            rsColumns = meta.getColumns(null, null, tblName, null);
            while (rsColumns.next()) {
                String columnName = rsColumns.getString("COLUMN_NAME");
                if (columnName.equalsIgnoreCase(tempColName)) {
                    String columnType = rsColumns.getString("TYPE_NAME");
                    if (columnType.equalsIgnoreCase("VARCHAR") || columnType.equalsIgnoreCase("CHAR")) {
                        flag = 1;
                    } else {
                        flag = 0;
                    }
                }
            }
             con.close();
             rsColumns.close();
        } catch (SQLException e) {
            System.out.println("error-102");
        }
        return flag;
    }

    public boolean isTimeStamp(String tempColName){
        boolean flag = false;
        dbMsg = msAccessConnect.getCurrentMsaccessConnection();
        if(dbMsg.getCODE() != 0){
            return false;
        }
        con = dbMsg.getDbCon();
        ResultSet rsColumns = null;
        try {
            DatabaseMetaData meta = con.getMetaData();
            rsColumns = meta.getColumns(null, null, tblName, null);
            while (rsColumns.next()) {
                String columnName = rsColumns.getString("COLUMN_NAME");
                if (columnName.equalsIgnoreCase(tempColName)) {
                    String columnType = rsColumns.getString("TYPE_NAME");
                    if (columnType.equalsIgnoreCase("DATE") || columnType.equalsIgnoreCase("TIME")
                            || columnType.equalsIgnoreCase("TIMESTAMP")) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            }
            con.close();
            rsColumns.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } 
        return flag;
    }
    
    public AppMessage dynSqlQuery(String colValues) {
        AppMessage appMsg = new AppMessage();
        dbMsg = mysqlConnect.getCurrentMysqlConnection();
        if(dbMsg.getCODE() != 0){
            appMsg.setCODE(dbMsg.getCODE());
            appMsg.setMSG(dbMsg.getMSG());
            return appMsg;
        }
        con = dbMsg.getDbCon();
        sqlQuery = "INSERT INTO " + tblName + " ( " + colNames + " ) VALUES ( " + colValues + " );";
        System.out.println(sqlQuery);
        try {
            Statement st = con.createStatement();
            //
            st.executeQuery("USE "+ CMNDBConfig.getMYSQL_DB_NAME() +";");
            int count = st.executeUpdate(sqlQuery);
            if (count > 0) {
                System.out.println("Updated....!!!");
                appMsg.setMSG("Inserted Successfully");
                appMsg.setCODE(0);
            } else {
                System.out.println("Failed....!!!");
                appMsg.setMSG("Failed to Insert");
                appMsg.setCODE(000);
            }
            con.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
            appMsg.setMSG(e.toString());
            appMsg.setCODE(101);
        } 
        return appMsg;
    }

    public boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9_]*$";
       //String pattern = "/^[a-z\\d\\-_\\s]+$/i";
        return s.matches(pattern);
    }
    
    public boolean isValid(String s){
        String pattern = "^[a-zA-Z0-9_ .,]*$";
        return s.matches(pattern);
    }
    
    public boolean isDateFormatCorrect(String s){
        String pattern = "^[0-9 ./:-]*$";
        return s.matches(pattern);
    }
}
