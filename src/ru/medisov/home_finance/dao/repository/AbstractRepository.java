package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    private static final String DELETE = "DELETE FROM currency_tbl WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new SimpleConnectionBuilder();

    public abstract T save(T model);

    public abstract Optional<T> findById(ID id);

    public abstract Collection<T> findAll();

    public abstract T update(T model);


    public boolean remove(Long aLong, Class oClass) {
        boolean isRemoved = false;
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
                preparedStatement.setLong(1, aLong);
                int rowsAffected = preparedStatement.executeUpdate();

                connection.commit();

                if (rowsAffected > 0) {
                    isRemoved = true;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while delete currency model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while delete currency model by id " + aLong, e);
        }

        return isRemoved;
    }
}
