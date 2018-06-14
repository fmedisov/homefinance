package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.DaoConfig;

import java.sql.Connection;

public class DbConnectionBuilder implements ConnectionBuilder
{
    private final static String DB_CONN_PARAMS = DaoConfig.getProperty("db.conn.params");
    private final static String DB_URL = DaoConfig.getProperty("db.url") + DB_CONN_PARAMS;
    private final static String DB_USER = DaoConfig.getProperty("db.login");
    private final static String DB_PASSWORD = DaoConfig.getProperty("db.password");

    @Override
    public Connection getConnection() {
        return getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}