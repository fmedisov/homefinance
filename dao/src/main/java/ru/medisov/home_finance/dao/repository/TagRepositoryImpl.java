package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.TagModel;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class TagRepositoryImpl extends AbstractRepository<TagModel, Long> implements TagRepository {
    private static final String INSERT = "INSERT INTO tag_tbl (name, count) VALUES (?, ?)";
    private static final String INSERT_TAG_LIST = "INSERT INTO tag_tbl (name, count) VALUES ";
    private static final String SELECT_BY_NAME = "SELECT id, name, count FROM tag_tbl t WHERE name = ?";
    private static final String SELECT_BY_NAME_LIST = "SELECT id, name, count FROM tag_tbl t WHERE name IN ";
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

    public TagRepositoryImpl() {}

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
        return find(SELECT_ALL);
    }

    @Override
    public Collection<TagModel> findByNames(List<TagModel> models) {
        return getNameList(models) == null ? new ArrayList<>() : findByNames(getNameList(models));
    }

    public Collection<TagModel> findByNames(String nameList) {
        return find(SELECT_BY_NAME_LIST + nameList);
    }

    private Collection<TagModel> find(String sql) {
        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        if (aLong == null) {
            return Optional.empty();
        }

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

    public List<TagModel> saveTagList(List<TagModel> models) {
        String namesAndCounts = getNamesAndCounts(models);

        if (namesAndCounts == null || namesAndCounts.length() == 0) {
            return models;
        }

        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TAG_LIST + namesAndCounts, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.executeUpdate();
                updateIdList(preparedStatement, models);
                connection.commit();
                return models;
            } catch (SQLException e) {
                connection.rollback();
                throw new HomeFinanceDaoException("error while save or replace tag model list " + models, e);
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException("error while save or replace tag model list " + models, e);
        }
    }

    public List<TagModel> updateTagList(List<TagModel> models) {
        if (models == null || models.size() == 0) {
            return models;
        }

        models.forEach(m -> m = update(m));

        return models;
    }

    private void updateIdList(PreparedStatement preparedStatement, List<TagModel> models) throws SQLException{
        if (models.stream().allMatch(tag -> tag.getId() != null)) {
            return;
        }

        List<Long> idList = new IdGetter(preparedStatement).getIdList();

        if (idList.size() == 0) {
            throw new SQLException("tags don't save or replace correctly");
        }

        for (int i = 0; i < models.size(); i++) {
            models.get(i).setId(idList.get(i));
        }
    }

    public List<TagModel> saveUpdateByTransaction(List<TagModel> allTags, Long transactionId) {
        //todo implement with fewer queries

        List<TagModel> existing = new ArrayList<>(findByNames(allTags));
        List<TagModel> notExisting = allTags.stream()
                .filter(t -> existing.stream()
                        .noneMatch(e -> e.getName().equals(t.getName())))
                .collect(Collectors.toList());

        List<TagModel> updated = updateByTransaction(existing, transactionId);
        List<TagModel> saved = saveByTransaction(notExisting, transactionId);

        List<TagModel> result = new ArrayList<>();

        result.addAll(updated);
        result.addAll(saved);

        return result;
    }

    private List<TagModel> saveByTransaction(List<TagModel> notExisting, Long transactionId) {
        List<TagModel> saved = saveTagList(notExisting);
        return updateByTransaction(saved, transactionId);
    }

    private List<TagModel> updateByTransaction(List<TagModel> existing, Long transactionId) {
        if (existing == null || existing.size() == 0) {
            return existing;
        }

        existing.forEach(tag -> {
            if (saveRelation(tag.getId(), transactionId)) {
                tag.setCount(tag.getCount() + 1);
            }
        });

        return updateTagList(existing);
    }

    public List<TagModel> findByTransaction(Long transactionId) {
        List<TagModel> tagModels = new ArrayList<>();

        try (Connection connection = connectionBuilder.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_TRANSACTION)) {
                preparedStatement.setLong(1, transactionId);
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

    public boolean removeByTransaction(Long transactionId) {
        return executeQueryByTransaction(UPDATE_TAG_COUNT, transactionId) &&
                executeQueryByTransaction(DELETE_BY_TRANSACTION, transactionId);
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

    private boolean executeQueryByTransaction(String query, Long aLong) {
        if (aLong == null) {
            return false;
        }

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
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RELATION, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, tagId);
                preparedStatement.setLong(2, modelId);
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

                return countRelations != 0;
            } catch (SQLException e) {
                throw new HomeFinanceDaoException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new HomeFinanceDaoException(e.getMessage());
        }
    }

    private String getNamesAndCounts(List<TagModel> tags) {
        if (tags == null || tags.size() == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        tags.forEach(tag -> builder.append("('").append(tag.getName()).append("',").append(tag.getCount()).append("),"));

        return builder.substring(0, builder.length() - 1);
    }

    private String getNameList(List<TagModel> tags) {
        if (tags == null || tags.size() == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        tags.forEach(tag -> builder.append("'").append(tag.getName()).append("',"));

        return parentheses(builder.substring(0, builder.length() - 1));
    }

    private String parentheses(String text) {
        return "(" + text + ")";
    }
}
