package org.ajikhoji.dripfeed.config;

public final class DbConfig {

    private DbConfig() { }

    public final static String DB_NAME = "DB_DRIP_PROBLEM";
    public final static String DB_URL = String.format("jdbc:mysql://localhost:3306/%s", DbConfig.DB_NAME);
    public final static String DB_USERNAME = System.getenv("DB_USER_NAME");
    public final static String DB_PASSWORD = System.getenv("DB_PASSWORD");

}
