package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.AccountModel;
import ru.medisov.home_finance.dao.model.AccountType;
import ru.medisov.home_finance.dao.model.CurrencyModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class AccountRepository implements Repository<AccountModel, Long> {
    private static final String INSERT = "INSERT INTO account_tbl (name, account_type, currency, amount) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ONE = "SELECT id, name, account_type, currency, amount FROM account_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, account_type, currency, amount FROM account_tbl";
    private static final String DELETE = "DELETE FROM account_tbl WHERE id = ?";
    private static final String UPDATE = "UPDATE account_tbl SET name = ?, account_type = ?, currency = ?, amount = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new SimpleConnectionBuilder();

    @Override
    public AccountModel save(AccountModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getAccountType().name());

                if (model.getCurrencyModel() == null) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setLong(3, model.getCurrencyModel().getId());
                }

                preparedStatement.setBigDecimal(4, model.getAmount());
                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());
                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while save account model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save account model " + model, e);
        }
    }

    @Override
    public Optional<AccountModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();

                AccountModel accountModel = null;

                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    AccountType accountType = Enum.valueOf(AccountType.class, resultSet.getString("account_type"));
                    Optional<CurrencyModel> optionalCurrency = new CurrencyRepository().findById(resultSet.getLong("currency"));
                    BigDecimal amount = resultSet.getBigDecimal("amount");

                    accountModel = new AccountModel(id, name, accountType, optionalCurrency.orElse(null), amount);
                }

                return Optional.ofNullable(accountModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find account model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find account model by id " + aLong, e);
        }
    }

    @Override
    public Collection<AccountModel> findAll() {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Collection<AccountModel> models = new ArrayList<>();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    AccountType accountType = Enum.valueOf(AccountType.class, resultSet.getString("account_type"));
                    Optional<CurrencyModel> optionalCurrency = new CurrencyRepository().findById(resultSet.getLong("currency"));
                    BigDecimal amount = resultSet.getBigDecimal("amount");

                    models.add(new AccountModel(id, name, accountType, optionalCurrency.orElse(null), amount));
                }

                return models;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find account models", e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find account models", e);
        }
    }

    @Override
    public boolean remove(Long aLong) {
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
                throw new HomeFinanceDaoException("error while delete account model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while delete account model by id " + aLong, e);
        }

        return isRemoved;
    }

    @Override
    public AccountModel update(AccountModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setString(2, model.getAccountType().name());

                if (model.getCurrencyModel() == null) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setLong(3, model.getCurrencyModel().getId());
                }

                preparedStatement.setBigDecimal(4, model.getAmount());

                preparedStatement.setLong(5, model.getId());
                preparedStatement.executeUpdate();

                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update account model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update account model " + model, e);
        }
    }
}