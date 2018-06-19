package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.common.model.TransactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TagRepository extends AbstractRepository<TagModel, Long> implements Repository<TagModel, Long> {
    private static final String INSERT = "INSERT INTO tag_tbl (name, count) VALUES (?, ?)";
    private static final String SELECT_BY_NAME = "SELECT id, name, count FROM tag_tbl t " +
                                        " WHERE name = ?";
    private static final String SELECT_BY_ID = "SELECT id, name, count FROM tag_tbl WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name, count FROM tag_tbl";
    private static final String UPDATE = "UPDATE tag_tbl SET name = ?, count = ? WHERE id = ?";

    private static final String INSERT_RELATION = "INSERT INTO tag_relation_tbl (tag, transaction) VALUES (?, ?)";
    private static final String CHECK_RELATION = "SELECT COUNT(*) AS countRelations FROM tag_relation_tbl " +
                                                                " WHERE tag = ? AND transaction = ?";
    private static final String UPDATE_TAG_COUNT = "UPDATE tag_tbl t SET t.count = t.count - 1 " +
                                                            "WHERE t.id = (SELECT tr.tag FROM tag_relation_tbl tr " +
                                                            "WHERE tr.tag = t.id AND tr.transaction = ?)";
    private static final String DELETE_BY_TRANSACTION = "DELETE FROM tag_relation_tbl WHERE transaction = ?";
    private static final String SELECT_BY_TRANSACTION = "SELECT t.id AS id, name, t.count " +
                                                            "FROM tag_tbl t, tag_relation_tbl tr " +
                                                            "WHERE tr.tag = t.id AND tr.transaction = ?";


    private ConnectionBuilder connectionBuilder = new DbConnectionBuilder();

    public TagRepository() {}

    @Override
    public Optional<TagModel> findByName(String name) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                TagModel tagModel = getTagModel(resultSet);

                return Optional.ofNullable(tagModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find tag model by name " + name, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find tag model by name " + name, e);
        }
    }

    @Override
    public Collection<TagModel> findAll() {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Collection<TagModel> models = new ArrayList<>();

                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Long count = resultSet.getLong("count");

                    TagModel tagModel = new TagModel().setId(id).setName(name).setCount(count);
                    models.add(tagModel);
                }

                return models;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find tag models", e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find tag models", e);
        }
    }

    @Override
    public boolean remove(Long aLong) {
        return super.remove(aLong, getClass());
    }

    @Override
    public TagModel save(TagModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setLong(2, model.getCount());
                preparedStatement.executeUpdate();
                model.setId(new IdGetter(preparedStatement).getId());
                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while save tag model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save tag model " + model, e);
        }
    }

    @Override
    public TagModel update(TagModel model) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setLong(2, model.getCount());
                preparedStatement.setLong(3, model.getId());
                preparedStatement.executeUpdate();

                connection.commit();
                return model;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while update tag model " + model, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while update tag model " + model, e);
        }
    }

    public Optional<TagModel> findById(Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
                preparedStatement.setLong(1, aLong);
                ResultSet resultSet = preparedStatement.executeQuery();
                TagModel tagModel = getTagModel(resultSet);

                return Optional.ofNullable(tagModel);
            } catch (SQLException e) {
                throw new HomeFinanceDaoException("error while find tag model by id " + aLong, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while find tag model by id " + aLong, e);
        }
    }

    private TagModel getTagModel(ResultSet resultSet) throws SQLException {
        TagModel tagModel = null;

        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String currentName = resultSet.getString("name");
            Long count = resultSet.getLong("count");

            tagModel = new TagModel().setId(id).setName(currentName).setCount(count);
        }
        return tagModel;
    }

    public List<TagModel> saveByTransaction(TransactionModel model) {
        //todo implement with fewer queries
        List<TagModel> updatedTags = new ArrayList<>();

        for (TagModel tag : model.getTags()) {
            TagModel foundInTagTable = findByName(tag.getName()).orElse(save(tag));
            if (saveRelation(foundInTagTable.getId(), model.getId())) {
                foundInTagTable.setCount(foundInTagTable.getCount() + 1);
                foundInTagTable = update(foundInTagTable);
            }

            updatedTags.add(foundInTagTable);
        }

        return updatedTags;
    }

    public List<TagModel> findByTransaction(TransactionModel model) {
        List<TagModel> tagModels = new ArrayList<>();

        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_TRANSACTION)) {
                preparedStatement.setLong(1, model.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Long count = resultSet.getLong("count");

                    TagModel tagModel = new TagModel().setId(id).setName(name).setCount(count);
                    tagModels.add(tagModel);
                }

                return tagModels;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException(e.getMessage());
        }
    }

    public boolean removeByTransaction(Long aLong) {
        return executeQueryByTransaction(UPDATE_TAG_COUNT, aLong) &&
                executeQueryByTransaction(DELETE_BY_TRANSACTION, aLong);
    }

    private boolean executeQueryByTransaction(String query, Long aLong) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, aLong);
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException(e.getMessage());
        }
    }

    private boolean saveRelation(Long tagId, Long modelId) {
        if (isRelationExist(tagId, modelId)) {
            return false;
        }

        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RELATION)) {
                preparedStatement.setLong(1, tagId);
                preparedStatement.setLong(2, modelId);
                preparedStatement.execute();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException(e.getMessage());
        }
    }

    private boolean isRelationExist(Long tagId, Long modelId) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_RELATION)) {
                preparedStatement.setLong(1, tagId);
                preparedStatement.setLong(2, modelId);
                ResultSet resultSet = preparedStatement.executeQuery();

                long countRelations = 0;

                if (resultSet.next()) {
                    countRelations = resultSet.getLong("countRelations");
                }

                connection.rollback();
                return countRelations != 0;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException(e.getMessage());
        }
    }
}
