package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IdGetter {
    private Statement statement;

    public IdGetter(Statement statement) {
        this.statement = statement;
    }

    public long getId() {
        try {
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while get id ", e);
        }

        return 0;
    }
}