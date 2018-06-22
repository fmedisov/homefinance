package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    private ConnectionBuilder connectionBuilder = new DbConnectionBuilder();

    public abstract T save(T model);

    public abstract Optional<T> findById(Long aLong);

    public abstract Collection<T> findAll();

    public abstract T update(T model);

    public boolean remove(Long aLong, Class oClass) {
        if (aLong == null) {
            return false;
        }

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
            case "CurrencyRepositoryImpl":
                tblName = "currency_tbl";
                break;
            case "CategoryRepositoryImpl":
                tblName = "category_tbl";
                break;
            case "AccountRepositoryImpl":
                tblName = "account_tbl";
                break;
            case "TransactionRepositoryImpl":
                tblName = "transaction_tbl";
                break;
            case "TagRepositoryImpl":
                tblName = "tag_tbl";
                break;
        }

        return tblName;
    }

    public CurrencyRepository getCurrencyRepository() {
        return new CurrencyRepositoryImpl();
    }

    public CategoryRepository getCategoryRepository() {
        return new CategoryRepositoryImpl();
    }

    public AccountRepository getAccountRepository() {
        return new AccountRepositoryImpl();
    }

    public TagRepository getTagRepository() {
        return new TagRepositoryImpl();
    }

    public TransactionRepository getTransactionRepository() {
        return new TransactionRepositoryImpl();
    }
}
