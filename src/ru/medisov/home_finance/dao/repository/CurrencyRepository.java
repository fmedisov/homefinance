package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CurrencyModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CurrencyRepository extends AbstractRepository<CurrencyModel, Long> implements Repository<CurrencyModel, Long> {
    private static final String INSERT = "INSERT INTO currency_tbl (name, code, symbol) VALUES (?, ?, ?)";
    private static final String SELECT_ONE = "SELECT id, name, code, symbol FROM currency_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, code, symbol FROM currency_tbl";
    private static final String DELETE = "DELETE FROM currency_tbl WHERE id = ?";
    private static final String UPDATE = "UPDATE currency_tbl SET name = ?, code = ?, symbol = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new SimpleConnectionBuilder();

    @Override
    public CurrencyModel save(CurrencyModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getCode());
                preparedStatement.setString(3, model.getSymbol());
                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());
                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while save currency model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save currency model " + model, e);
        }
    }

    @Override
    public Optional<CurrencyModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();

                CurrencyModel currencyModel = null;

                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String code = resultSet.getString("code");
                    String symbol = resultSet.getString("symbol");

                    currencyModel = new CurrencyModel(id, name, code, symbol);
                }

                return Optional.ofNullable(currencyModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find currency model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find currency model by id " + aLong, e);
        }
    }

    @Override
    public Collection<CurrencyModel> findAll() {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Collection<CurrencyModel> models = new ArrayList<>();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String code = resultSet.getString("code");
                    String symbol = resultSet.getString("symbol");

                    models.add(new CurrencyModel(id, name, code, symbol));
                }

                return models;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find currency models", e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find currency models", e);
        }
    }

    @Override
    public boolean remove(Long aLong) {
        return super.remove(aLong, CurrencyRepository.class);
    }

//    @Override
//    public boolean remove(Long aLong) {
//        boolean isRemoved = false;
//        try (Connection connection = connectionBuilder.getConnection()) {
//            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
//                preparedStatement.setLong(1, aLong);
//                int rowsAffected = preparedStatement.executeUpdate();
//
//                connection.commit();
//
//                if (rowsAffected > 0) {
//                    isRemoved = true;
//                }
//            } catch (SQLException e) {
//                connection.rollback();
//                throw new HomeFinanceDaoException("error while delete currency model by id " + aLong, e);
//            }
//        } catch (SQLException e) {
//            throw new HomeFinanceDaoException("error while delete currency model by id " + aLong, e);
//        }
//
//        return isRemoved;
//    }

    @Override
    public CurrencyModel update(CurrencyModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getCode());
                preparedStatement.setString(3, model.getSymbol());
                preparedStatement.setLong(4, model.getId());
                preparedStatement.executeUpdate();

                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update currency model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update currency model " + model, e);
        }
    }
}
