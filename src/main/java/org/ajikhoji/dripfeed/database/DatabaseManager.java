package org.ajikhoji.dripfeed.database;

import org.ajikhoji.dripfeed.config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager dManager;

    private Connection con = null;

    public static DatabaseManager getInstance() {
        synchronized (DatabaseManager.class) {
            if(dManager == null) {
                dManager = new DatabaseManager();
            }
        }
        return dManager;
    }

    private DatabaseManager() {
        this.initConnection();
    }

    private void initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection(DbConfig.DB_URL, DbConfig.DB_USERNAME, DbConfig.DB_PASSWORD);
        } catch (final ClassNotFoundException | SQLException e) {
            System.err.println(DatabaseManager.class.getCanonicalName() + " - initConnection(): Error in opening database.");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            this.con.close();
        } catch (final Exception e) {
            System.err.println("Connection close failed");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.con;
    }

}
