package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ConnectionBuilder
{
    Connection getConnection();

    default Connection getConnection(String dbUrl, String dbUser, String dbPassword) {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while get DB connection", e);
        }
    }
}