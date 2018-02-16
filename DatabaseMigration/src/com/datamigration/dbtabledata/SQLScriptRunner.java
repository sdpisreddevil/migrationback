package com.datamigration.dbtabledata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.datamigration.dbconnection.MysqlConnect;
import com.datamigration.scripts.DBMessage;
import com.ibatis.common.jdbc.ScriptRunner;
import java.io.IOException;
import java.sql.SQLException;

public class SQLScriptRunner extends Thread {

    private String sqlFilePath = "dump.sql";
    private MysqlConnect mySqlConnect;
    private DBMessage dbMsg;
    private Connection con;
    private JPanel rootPanel;
    
    public SQLScriptRunner(JPanel rootPanel) {
        mySqlConnect = new MysqlConnect();
        dbMsg = mySqlConnect.getCurrentMysqlConnection();
        if (dbMsg.getCODE() != 0) {
            JOptionPane.showMessageDialog(null, dbMsg.getMSG());
            return;
        }
        con = dbMsg.getDbCon();
        this.rootPanel = rootPanel;
    }

    public void runSQLScript() {
        try {
            //Initializing object for ScriptRunner
            ScriptRunner sr = new ScriptRunner(con, false, rootPanel);
            //Giving the input sql file to the Reader
            Reader reader = new BufferedReader(new FileReader(sqlFilePath));
            //Executing sql script
            sr.runScript(reader);
        } catch (IOException | SQLException e) {
            System.out.println(e.getCause());
        }
    }

    public void run() {
        runSQLScript();
    }
}
