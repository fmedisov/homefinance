package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionRepository implements Repository<TransactionModel, Long> {
    private static final String INSERT = "INSERT INTO transaction_tbl (amount, datetime, account, category, transaction_type, tags) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ONE = "SELECT id, amount, datetime, account, category, transaction_type, tags FROM transaction_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, amount, datetime, account, category, transaction_type, tags FROM transaction_tbl";
    private static final String DELETE = "DELETE FROM transaction_tbl WHERE id = ?";
    private static final String UPDATE = "UPDATE transaction_tbl SET amount = ?, datetime = ?, account = ?, category = ?, transaction_type = ?, tags = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new SimpleConnectionBuilder();

    @Override
    public TransactionModel save(TransactionModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setBigDecimal(1, model.getAmount());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(model.getDateTime()));

                if (model.getAccount() == null) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setLong(3, model.getAccount().getId());
                }

                if (model.getCategory() == null) {
                    preparedStatement.setNull(4, Types.INTEGER);
                } else {
                    preparedStatement.setLong(4, model.getCategory().getId());
                }

                preparedStatement.setString(5, model.getTransactionType().name());
                preparedStatement.setString(6, model.getTags().toString());
                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());
                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while save transaction model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save transaction model " + model, e);
        }
    }

    @Override
    public Optional<TransactionModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();

                TransactionModel transactionModel = null;

                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
                    Optional<AccountModel> optionalAccount = new AccountRepository().findById(resultSet.getLong("account"));
                    Optional<CategoryTransactionModel> optionalCategory = new CategoryRepository().findById(resultSet.getLong("category"));
                    TransactionType transactionType = Enum.valueOf(TransactionType.class, resultSet.getString("transaction_type"));
                    List<String> tags = Arrays.asList(resultSet.getString("tags").split(","));

                    transactionModel = new TransactionModel(amount, dateTime, optionalAccount.orElse(null),
                                optionalCategory.orElse(null), transactionType, tags);
                    transactionModel.setId(id);
                }

                return Optional.ofNullable(transactionModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find transaction model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find transaction model by id " + aLong, e);
        }
    }

    @Override
    public Collection<TransactionModel> findAll() {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Collection<TransactionModel> models = new ArrayList<>();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
                    Optional<AccountModel> optionalAccount = new AccountRepository().findById(resultSet.getLong("account"));
                    Optional<CategoryTransactionModel> optionalCategory = new CategoryRepository().findById(resultSet.getLong("category"));
                    TransactionType transactionType = Enum.valueOf(TransactionType.class, resultSet.getString("transaction_type"));
                    String tagsString = resultSet.getString("tags");

                    List<String> tags = tagsString == null ? null : Arrays.asList(tagsString.split(","));

                    TransactionModel model = new TransactionModel(amount, dateTime, optionalAccount.orElse(null),
                            optionalCategory.orElse(null), transactionType, tags);
                    model.setId(id);

                    models.add(model);
                }

                return models;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find transaction models", e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find transaction models", e);
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
                throw new HomeFinanceDaoException("error while delete transaction model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while delete transaction model by id " + aLong, e);
        }

        return isRemoved;
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                preparedStatement.setBigDecimal(1, model.getAmount());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(model.getDateTime()));

                if (model.getAccount() == null) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setLong(3, model.getAccount().getId());
                }

                if (model.getCategory() == null) {
                    preparedStatement.setNull(4, Types.INTEGER);
                } else {
                    preparedStatement.setLong(4, model.getCategory().getId());
                }

                preparedStatement.setString(5, model.getTransactionType().name());
                preparedStatement.setString(6, model.getTags().toString());

                preparedStatement.setLong(7, model.getId());
                preparedStatement.executeUpdate();

                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update transaction model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update transaction model " + model, e);
        }
    }
}