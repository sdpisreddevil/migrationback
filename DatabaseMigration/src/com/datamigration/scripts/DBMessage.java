package com.datamigration.scripts;

import java.sql.Connection;

public class DBMessage extends AppMessage{
    private Connection dbCon;

    public Connection getDbCon() {
        return dbCon;
    }

    public void setDbCon(Connection dbCon) {
        this.dbCon = dbCon;
    }
}
