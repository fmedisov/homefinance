package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class IdGetter {
    private Statement statement;

    public IdGetter(Statement statement) {
        this.statement = statement;
    }

    public Long getId() {
        try {
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while get id ", e);
        }

        return 0L;
    }

    public List<Long> getIdList() {
        try {
            List<Long> result = new ArrayList<>();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }

            return result;
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while get id list ", e);
        }
    }
}