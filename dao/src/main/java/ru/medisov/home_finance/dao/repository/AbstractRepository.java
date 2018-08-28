package ru.medisov.home_finance.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Transactional
public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    @Autowired
    private DataSource dataSource;

    public abstract T save(T model);

    public abstract Optional<T> findById(Long aLong);

    public abstract Collection<T> findAll();

    public abstract T update(T model);

    public boolean remove(Long aLong, Class oClass) {
        if (aLong == null) {
            return false;
        }

        boolean isRemoved = false;

        try (Connection connection = dataSource.getConnection()) {
            String tblName = getTableName(oClass);
            if (tblName == null) {
                throw new HomeFinanceDaoException("error while delete model by id: unknown table ");
            }
            String deleteQuery = "DELETE FROM " + tblName + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setLong(1, aLong);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    isRemoved = true;
                }
            } catch (SQLException e) {
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
//            case "CurrencyRepositoryImpl":
//                tblName = "currency_tbl";
//                break;
//            case "CategoryRepositoryImpl":
//                tblName = "category_tbl";
//                break;
//            case "AccountRepositoryImpl":
//                tblName = "account_tbl";
//                break;
//            case "TransactionRepositoryImpl":
//                tblName = "transaction_tbl";
//                break;
//            case "TagRepositoryImpl":
//                tblName = "tag_tbl";
//                break;
        }

        return tblName;
    }
}
