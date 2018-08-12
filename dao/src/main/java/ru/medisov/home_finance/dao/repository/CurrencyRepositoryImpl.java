package ru.medisov.home_finance.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CurrencyModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Transactional
public class CurrencyRepositoryImpl extends AbstractRepository<CurrencyModel, Long> implements CurrencyRepository {
    private static final String INSERT = "INSERT INTO currency_tbl (name, code, symbol) VALUES (?, ?, ?)";
    private static final String SELECT_BY_NAME = "SELECT id, name, code, symbol FROM currency_tbl WHERE name = ?";
    private static final String SELECT_BY_ID = "SELECT id, name, code, symbol FROM currency_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, code, symbol FROM currency_tbl";
    private static final String UPDATE = "UPDATE currency_tbl SET name = ?, code = ?, symbol = ? WHERE id = ?";

    @Autowired
    private DataSource dataSource;

    @Override
    public CurrencyModel save(CurrencyModel model) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getCode());
                preparedStatement.setString(3, model.getSymbol());

                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());
                return model;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while save currency model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save currency model " + model, e);
        }
    }

    @Override
    public Optional<CurrencyModel> findByName(String name) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                CurrencyModel currencyModel = getCurrencyModel(resultSet);

                return Optional.ofNullable(currencyModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find currency model by name " + name, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find currency model by name " + name, e);
        }
    }

    @Override
    public Collection<CurrencyModel> findAll() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Collection<CurrencyModel> models = new ArrayList<>();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String code = resultSet.getString("code");
                    String symbol = resultSet.getString("symbol");

                    CurrencyModel currencyModel = new CurrencyModel().setId(id).setName(name).setCode(code).setSymbol(symbol);
                    models.add(currencyModel);
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
        return super.remove(aLong, getClass());
    }

    @Override
    public CurrencyModel update(CurrencyModel model) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getCode());
                preparedStatement.setString(3, model.getSymbol());
                preparedStatement.setLong(4, model.getId());
                preparedStatement.executeUpdate();

                return model;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while update currency model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update currency model " + model, e);
        }
    }

    @Override
    public Optional<CurrencyModel> findById(Long aLong) {
        if (aLong == null) {
            return Optional.empty();
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();
                CurrencyModel currencyModel = getCurrencyModel(resultSet);

                return Optional.ofNullable(currencyModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find currency model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find currency model by id " + aLong, e);
        }
    }

    private CurrencyModel getCurrencyModel(ResultSet resultSet) throws SQLException {
        CurrencyModel currencyModel = null;

        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String currentName = resultSet.getString("name");
            String code = resultSet.getString("code");
            String symbol = resultSet.getString("symbol");

            currencyModel = new CurrencyModel().setId(id).setName(currentName).setCode(code).setSymbol(symbol);
        }
        return currencyModel;
    }
}
