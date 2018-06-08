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

public class AccountRepository extends AbstractRepository<AccountModel, Long> implements Repository<AccountModel, Long> {
    private static final String INSERT = "INSERT INTO account_tbl (name, account_type, currency, amount) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_NAME = "SELECT id, name, account_type, currency, amount FROM account_tbl WHERE name = ?";
    private static final String SELECT_BY_ID = "SELECT id, name, account_type, currency, amount FROM account_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, account_type, currency, amount FROM account_tbl";
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
    public Optional<AccountModel> findByName(String name) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                AccountModel accountModel = getAccountModel(resultSet);

                return Optional.ofNullable(accountModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find account model by name " + name, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find account model by name " + name, e);
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

                    AccountModel accountModel = new AccountModel().setId(id).setName(name).setAccountType(accountType)
                            .setCurrencyModel(optionalCurrency.orElse(null)).setAmount(amount);
                    models.add(accountModel);
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
        return super.remove(aLong, getClass());
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

    public Optional<AccountModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();
                AccountModel accountModel = getAccountModel(resultSet);

                return Optional.ofNullable(accountModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find account model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find account model by id " + aLong, e);
        }
    }

    private AccountModel getAccountModel(ResultSet resultSet) throws SQLException {
        AccountModel accountModel = null;
        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String currentName = resultSet.getString("name");
            AccountType accountType = Enum.valueOf(AccountType.class, resultSet.getString("account_type"));
            Optional<CurrencyModel> optionalCurrency = new CurrencyRepository().findById(resultSet.getLong("currency"));
            BigDecimal amount = resultSet.getBigDecimal("amount");

            accountModel = new AccountModel().setId(id).setName(currentName).setAccountType(accountType)
                    .setCurrencyModel(optionalCurrency.orElse(null)).setAmount(amount);
        }
        return accountModel;
    }
}