package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CategoryTransactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CategoryRepository implements Repository<CategoryTransactionModel, Long> {
    private static final String INSERT = "INSERT INTO category_tbl (name, parent) VALUES (?, ?)";
    private static final String SELECT_ONE = "SELECT id, name, parent FROM category_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, parent FROM category_tbl";
    private static final String DELETE = "DELETE FROM category_tbl WHERE id = ?";
    private static final String UPDATE = "UPDATE category_tbl SET name = ?, parent = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new SimpleConnectionBuilder();

    @Override
    public CategoryTransactionModel save(CategoryTransactionModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, model.getName());

                if (model.getParent() == null) {
                    preparedStatement.setNull(2, Types.INTEGER);
                } else {
                    preparedStatement.setLong(2, model.getParent().getId());
                }

                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());
                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while save category transaction model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save category transaction model " + model, e);
        }
    }

    @Override
    public Optional<CategoryTransactionModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();

                CategoryTransactionModel categoryTransactionModel = null;

                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    long parentId = resultSet.getLong("parent");

                    Optional<CategoryTransactionModel> parent = findById(parentId);

                    categoryTransactionModel = new CategoryTransactionModel(id, name, parent.orElse(null));
                }

                return Optional.ofNullable(categoryTransactionModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find category transaction model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find category transaction model by id " + aLong, e);
        }
    }

    @Override
    public Collection<CategoryTransactionModel> findAll() {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Collection<CategoryTransactionModel> models = new ArrayList<>();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    long parentId = resultSet.getLong("parent");

                    Optional<CategoryTransactionModel> parent = findById(parentId);
                    models.add(new CategoryTransactionModel(id, name, parent.orElse(null)));
                }

                return models;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find category transaction models", e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find category transaction models", e);
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
                throw new HomeFinanceDaoException("error while delete category transaction model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while delete category transaction model by id " + aLong, e);
        }

        return isRemoved;
    }

    @Override
    public CategoryTransactionModel update(CategoryTransactionModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                preparedStatement.setString(1, model.getName());

                if (model.getParent() == null) {
                    preparedStatement.setNull(2, Types.INTEGER);
                } else {
                    preparedStatement.setLong(2, model.getParent().getId());
                }

                preparedStatement.setLong(3, model.getId());
                preparedStatement.executeUpdate();

                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update category transaction model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update category transaction model " + model, e);
        }
    }
}