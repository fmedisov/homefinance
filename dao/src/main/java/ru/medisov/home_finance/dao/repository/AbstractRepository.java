package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    private ConnectionBuilder connectionBuilder = new SimpleConnectionBuilder();

    public abstract T save(T model);

    public abstract Optional<T> findByName(String name);

    public abstract Collection<T> findAll();

    public abstract T update(T model);

    public boolean remove(Long aLong, Class oClass) {
        boolean isRemoved = false;
        try (Connection connection = connectionBuilder.getConnection()) {
            String tblName = getTableName(oClass);
            if (tblName == null) {
                throw new HomeFinanceDaoException("error while delete model by id: unknown table ");
            }
            String deleteQuery = "DELETE FROM " + tblName + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setLong(1, aLong);
                int rowsAffected = preparedStatement.executeUpdate();

                connection.commit();

                if (rowsAffected > 0) {
                    isRemoved = true;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while delete model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while delete model by id " + aLong, e);
        }

        return isRemoved;
    }

    private String getTableName(Class oClass) {
        String tblName = null;
        switch (oClass.getSimpleName()) {
            case "CurrencyRepository":
                tblName = "currency_tbl";
                break;
            case "CategoryRepository":
                tblName = "category_tbl";
                break;
            case "AccountRepository":
                tblName = "account_tbl";
                break;
            case "TransactionRepository":
                tblName = "transaction_tbl";
                break;
        }

        return tblName;
    }
}
