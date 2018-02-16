
package com.datamigration.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.datamigration.scripts.CMNDBConfig;
import com.datamigration.scripts.DBMessage;

public class MSAccessConnect {

    private  String path = CMNDBConfig.getMSACCESS_PATH()+";password=";
    private  String pwd = CMNDBConfig.getMSACCESS_PWD();
    private  Connection connection = null;
    private  DBMessage dbMsg;

    public MSAccessConnect(){
        dbMsg = new DBMessage();
    }
    public DBMessage getCurrentMsaccessConnection() {
            try {
                connection = DriverManager.getConnection("jdbc:ucanaccess://" + path + pwd +";memory=false");
                dbMsg.setCODE(0);
                dbMsg.setMSG("MS Access Connection Established!");
                dbMsg.setDbCon(connection);
            } catch (SQLException ex) {
                dbMsg.setCODE(101);
               System.out.println(ex.toString());
               dbMsg.setMSG(ex.getMessage());
            }
        return dbMsg;
    }
}
