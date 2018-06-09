package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.DaoConfig;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionBuilder implements ConnectionBuilder
{
    private final static String DB_CONN_PARAMS = DaoConfig.getProperty("db.conn.params");
    private final static String DB_URL = DaoConfig.getProperty("db.url") + DB_CONN_PARAMS;
    private final static String DB_USER = DaoConfig.getProperty("db.login");
    private final static String DB_PASSWORD = DaoConfig.getProperty("db.password");

    @Override
    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while get DB connection", e);
        }
    }
}