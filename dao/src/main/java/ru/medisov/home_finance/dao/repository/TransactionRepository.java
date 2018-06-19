package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.model.TransactionType;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionRepository extends AbstractRepository<TransactionModel, Long> implements Repository<TransactionModel, Long> {
    private static final String INSERT = "INSERT INTO transaction_tbl (amount, datetime, account, category, transaction_type, name) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_NAME = "SELECT id, amount, datetime, account, category, transaction_type, name FROM transaction_tbl WHERE name = ?";
    private static final String SELECT_ALL = "SELECT id, amount, datetime, account, category, transaction_type, name FROM transaction_tbl";
    private static final String UPDATE = "UPDATE transaction_tbl SET amount = ?, datetime = ?, account = ?, category = ?, transaction_type = ?, name = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new DbConnectionBuilder();

    public TransactionRepository() {}

    @Override
    public TransactionModel save(TransactionModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                updateCategoryModel(model);
                updateAccountModel(model);
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

                if (model.getTransactionType() == null) {
                    throw new SQLException("transaction type must have a value");
                }

                preparedStatement.setString(5, model.getTransactionType().name());
                preparedStatement.setString(6, model.getName());
                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());

                connection.commit();

                model.setTags(new TagRepository().saveByTransaction(model));
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
    public Optional<TransactionModel> findByName(String name) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                TransactionModel transactionModel = getTransactionModel(resultSet);

                return Optional.ofNullable(transactionModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find transaction model by name " + name, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find transaction model by name " + name, e);
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
                    String name = resultSet.getString("name");

                    TransactionModel model = new TransactionModel().setId(id).setAmount(amount).setDateTime(dateTime)
                            .setAccount(optionalAccount.orElse(null)).setCategory(optionalCategory.orElse(null))
                            .setTransactionType(transactionType).setName(name);

                    model.setTags(new TagRepository().findByTransaction(model));

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
        return new TagRepository().removeByTransaction(aLong) && super.remove(aLong, getClass());
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                updateCategoryModel(model);
                updateAccountModel(model);
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

                if (model.getTransactionType() == null) {
                    throw new SQLException("transaction type must have a value");
                }

                preparedStatement.setString(5, model.getTransactionType().name());
                preparedStatement.setString(6, model.getName());

                preparedStatement.setLong(7, model.getId());
                preparedStatement.executeUpdate();

                connection.commit();

                model.setTags(new TagRepository().saveByTransaction(model));
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update transaction model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update transaction model " + model, e);
        }
    }

    private TransactionModel getTransactionModel(ResultSet resultSet) throws SQLException {
        TransactionModel transactionModel = null;
        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            BigDecimal amount = resultSet.getBigDecimal("amount");
            LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
            Optional<AccountModel> optionalAccount = new AccountRepository().findById(resultSet.getLong("account"));
            Optional<CategoryTransactionModel> optionalCategory = new CategoryRepository().findById(resultSet.getLong("category"));
            TransactionType transactionType = Enum.valueOf(TransactionType.class, resultSet.getString("transaction_type"));
            String currentName = resultSet.getString("name");

            transactionModel = new TransactionModel().setId(id).setAmount(amount).setDateTime(dateTime)
                    .setAccount(optionalAccount.orElse(null)).setCategory(optionalCategory.orElse(null))
                    .setTransactionType(transactionType).setName(currentName);

            transactionModel.setTags(new TagRepository().findByTransaction(transactionModel));
        }
        return transactionModel;
    }

    public Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return new ArrayList<>();
    }

    public Collection<TransactionModel> findByCategory(long categoryId) {
        return new ArrayList<>();
    }

    public BigDecimal incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return BigDecimal.ZERO;
    }

    public BigDecimal expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> incomeByCategory(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return new HashMap<>();
    }

    public Map<String, BigDecimal> expenseByCategory(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return new HashMap<>();
    }

    public boolean removeByAccount(Long accountId) {
        return false;
    }

    private void updateCategoryModel(TransactionModel model) {
        if (model.getCategory() != null && model.getCategory().getId() == 0) {
            model.setCategory(new CategoryRepository().saveWithParents(model.getCategory()));
        }
    }

    private void updateAccountModel(TransactionModel model) {
        if (model.getAccount() != null && model.getAccount().getId() == 0) {
            model.setAccount(new AccountRepository().save(model.getAccount()));
        }
    }
}