package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CategoryRepository extends AbstractRepository<CategoryTransactionModel, Long> implements Repository<CategoryTransactionModel, Long> {
    private static final String INSERT = "INSERT INTO category_tbl (name, parent) VALUES (?, ?)";
    private static final String SELECT_BY_NAME = "SELECT id, name, parent FROM category_tbl WHERE name = ?";
    private static final String SELECT_BY_ID = "SELECT id, name, parent FROM category_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, parent FROM category_tbl";
    private static final String UPDATE = "UPDATE category_tbl SET name = ?, parent = ? WHERE id = ?";

    private ConnectionBuilder connectionBuilder = new DbConnectionBuilder();

    public CategoryRepository() {}

    public CategoryRepository(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

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
    public Optional<CategoryTransactionModel> findByName(String name) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                CategoryTransactionModel categoryTransactionModel = getCategoryTransactionModel(resultSet);

                return Optional.ofNullable(categoryTransactionModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find category transaction model by name " + name, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find category transaction model by name " + name, e);
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
                    CategoryTransactionModel categoryTransactionModel = new CategoryTransactionModel().setId(id).setName(name)
                            .setParent(parent.orElse(null));
                    models.add(categoryTransactionModel);
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
        return super.remove(aLong, getClass());
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

    public CategoryTransactionModel saveWithParents(CategoryTransactionModel model) {
        CategoryTransactionModel parent = model.getParent();

        if (parent != null) {
            model.setParent(saveWithParents(parent));
        }

        return save(model);
    }

    public Optional<CategoryTransactionModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();
                CategoryTransactionModel categoryTransactionModel = getCategoryTransactionModel(resultSet);

                return Optional.ofNullable(categoryTransactionModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find category transaction model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find category transaction model by id " + aLong, e);
        }
    }

    private CategoryTransactionModel getCategoryTransactionModel(ResultSet resultSet) throws SQLException {
        CategoryTransactionModel categoryTransactionModel = null;
        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String currentName = resultSet.getString("name");
            long parentId = resultSet.getLong("parent");
            Optional<CategoryTransactionModel> parent = findById(parentId);
            categoryTransactionModel = new CategoryTransactionModel().setId(id).setName(currentName)
                    .setParent(parent.orElse(null));
        }
        return categoryTransactionModel;
    }
}