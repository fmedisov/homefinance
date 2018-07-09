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

public class TransactionRepositoryImpl extends AbstractRepository<TransactionModel, Long> implements TransactionRepository {
    private static final String FIELDS = "id, amount, datetime, account, category, transaction_type, name";
    private static final String INSERT = "INSERT INTO transaction_tbl (amount, datetime, account, category, transaction_type, name) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_NAME = "SELECT " + FIELDS + " FROM transaction_tbl WHERE name = ?";
    private static final String SELECT_BY_ID = "SELECT " + FIELDS + " FROM transaction_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT " + FIELDS + " FROM transaction_tbl";
    private static final String SELECT_BY_PERIOD = "SELECT " + FIELDS + " FROM transaction_tbl WHERE datetime >= ? AND datetime <= ?";
    private static final String SELECT_INCOME_BY_PERIOD = "SELECT " + FIELDS + " FROM transaction_tbl WHERE transaction_type = 'Income' AND datetime >= ? AND datetime <= ?";
    private static final String SELECT_EXPENSE_BY_PERIOD = "SELECT " + FIELDS + " FROM transaction_tbl WHERE transaction_type = 'Expense' AND datetime >= ? AND datetime <= ?";
    private static final String SELECT_BY_CATEGORY = "SELECT " + FIELDS + " FROM transaction_tbl WHERE category = ?";
    private static final String SELECT_BY_NULL_CATEGORY = "SELECT " + FIELDS + " FROM transaction_tbl WHERE category IS NULL";
    private static final String UPDATE = "UPDATE transaction_tbl SET amount = ?, datetime = ?, account = ?, category = ?, transaction_type = ?, name = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new DbConnectionBuilder();

    public TransactionRepositoryImpl() {}

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

                model.setTags(getTagRepository().saveUpdateByTransaction(model.getTags(), model.getId()));

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
    public Optional<TransactionModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();
                TransactionModel transactionModel = getTransactionModel(resultSet);

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
        return find(SELECT_ALL, null, null);
    }

    @Override
    public boolean remove(Long aLong) {
        return getTagRepository().removeByTransaction(aLong) && super.remove(aLong, getClass());
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

                model.setTags(getTagRepository().saveUpdateByTransaction(model.getTags(), model.getId()));
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update transaction model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update transaction model " + model, e);
        }
    }

    @Override
    public Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return find(SELECT_BY_PERIOD, Arrays.asList(dateFrom, upToDate), LocalDateTime.class);
    }

    @Override
    public Collection<TransactionModel> findByCategory(Long categoryId) {
        if (categoryId == null) {
            return find(SELECT_BY_NULL_CATEGORY, null, null);
        } else {
            return find(SELECT_BY_CATEGORY, Arrays.asList(categoryId), Long.class);
        }
    }

    @Override
    public Collection<TransactionModel> incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return find(SELECT_INCOME_BY_PERIOD, Arrays.asList(dateFrom, upToDate), LocalDateTime.class);
    }

    @Override
    public Collection<TransactionModel> expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return find(SELECT_EXPENSE_BY_PERIOD, Arrays.asList(dateFrom, upToDate), LocalDateTime.class);
    }

    @Override
    public boolean removeByAccount(Long accountId) {
        return false;
    }

    private void updateCategoryModel(TransactionModel model) {
        if (model.getCategory() != null && model.getCategory().getId() == null) {
            model.setCategory(getCategoryRepository().saveWithParents(model.getCategory()));
        }
    }

    private void updateAccountModel(TransactionModel model) {
        if (model.getAccount() != null && model.getAccount().getId() == null) {
            model.setAccount(getAccountRepository().save(model.getAccount()));
        }
    }

    private TransactionModel getTransactionModel(ResultSet resultSet) throws SQLException {
        TransactionModel transactionModel = null;
        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            BigDecimal amount = resultSet.getBigDecimal("amount");
            LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
            Optional<AccountModel> optionalAccount = getAccountRepository().findById(resultSet.getLong("account"));
            Optional<CategoryTransactionModel> optionalCategory = getCategoryRepository().findById(resultSet.getLong("category"));
            TransactionType transactionType = Enum.valueOf(TransactionType.class, resultSet.getString("transaction_type"));
            String currentName = resultSet.getString("name");

            transactionModel = new TransactionModel().setId(id).setAmount(amount).setDateTime(dateTime)
                    .setAccount(optionalAccount.orElse(null)).setCategory(optionalCategory.orElse(null))
                    .setTransactionType(transactionType).setName(currentName);

            transactionModel.setTags(getTagRepository().findByTransaction(transactionModel.getId()));
        }
        return transactionModel;
    }

    private Collection<TransactionModel> getTransactionModels(ResultSet resultSet) throws SQLException {
        Collection<TransactionModel> models = new ArrayList<>();

        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            BigDecimal amount = resultSet.getBigDecimal("amount");
            LocalDateTime dateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
            Optional<AccountModel> optionalAccount = getAccountRepository().findById(resultSet.getLong("account"));
            Optional<CategoryTransactionModel> optionalCategory = getCategoryRepository().findById(resultSet.getLong("category"));
            TransactionType transactionType = Enum.valueOf(TransactionType.class, resultSet.getString("transaction_type"));
            String name = resultSet.getString("name");

            TransactionModel model = new TransactionModel().setId(id).setAmount(amount).setDateTime(dateTime)
                    .setAccount(optionalAccount.orElse(null)).setCategory(optionalCategory.orElse(null))
                    .setTransactionType(transactionType).setName(name);

            model.setTags(getTagRepository().findByTransaction(model.getId()));

            models.add(model);
        }

        return models;
    }

    private Collection<TransactionModel> find(String selectQuery, List properties, Class propertyClass) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                if (properties != null && properties.size() > 0) {
                    setStatementProperties(preparedStatement, properties, propertyClass);
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                return getTransactionModels(resultSet);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find transaction models", e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find transaction models", e);
        }
    }

    private void setStatementProperties(PreparedStatement statement, List properties, Class propertyClass) throws SQLException {
        for (int i = 0; i < properties.size(); i++) {
            if ("LocalDateTime".equals(propertyClass.getSimpleName())) {
                if (properties.get(i) == null) {
                    statement.setNull(i + 1, Types.TIMESTAMP);
                } else {
                    statement.setTimestamp(i + 1, Timestamp.valueOf((LocalDateTime) properties.get(i)));
                }
            } else if ("Long".equals(propertyClass.getSimpleName())) {
                if (properties.get(i) == null) {
                    statement.setNull(i + 1, Types.INTEGER);
                } else {
                    statement.setLong(i + 1, (Long) properties.get(i));
                }
            }
        }
    }
}