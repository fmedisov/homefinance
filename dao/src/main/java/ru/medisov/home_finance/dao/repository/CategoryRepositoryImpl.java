package ru.medisov.home_finance.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CategoryRepositoryImpl extends AbstractRepository<CategoryTransactionModel, Long> implements CategoryRepository {
//    private static final String INSERT = "INSERT INTO category_tbl (name, parent) VALUES (?, ?)";
//    private static final String SELECT_BY_NAME = "SELECT id, name, parent FROM category_tbl WHERE name = ?";
//    private static final String SELECT_BY_ID = "SELECT id, name, parent FROM category_tbl WHERE id = ?";
//    private static final String SELECT_ALL = "SELECT id, name, parent FROM category_tbl";
//    private static final String UPDATE = "UPDATE category_tbl SET name = ?, parent = ? WHERE id = ?";
//
//    @Autowired
//    private DataSource dataSource;

    @PersistenceContext
    private EntityManager em;

    @Override
    public CategoryTransactionModel save(CategoryTransactionModel model) {
        em.persist(model);
        return model;
    }

    @Override
    public Optional<CategoryTransactionModel> findByName(String name) {
        try {
            Query query = em.createNamedQuery("CategoryTransactionModel.findByName", CategoryTransactionModel.class);
            query.setParameter("name", name);
            return Optional.ofNullable((CategoryTransactionModel) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<CategoryTransactionModel> findAll() {
        return em.createNamedQuery("CategoryTransactionModel.findAll", CategoryTransactionModel.class).getResultList();
    }

    @Override
    public boolean remove(Long aLong) {
        em.remove(em.find(CategoryTransactionModel.class, aLong));
        return true;
    }

    @Override
    public CategoryTransactionModel update(CategoryTransactionModel model) {
        return em.merge(model);
    }

    public Optional<CategoryTransactionModel> findById(Long aLong) {
        return Optional.ofNullable(em.find(CategoryTransactionModel.class, aLong));
    }

    public CategoryTransactionModel saveWithParents(CategoryTransactionModel model) {
        CategoryTransactionModel parent = model.getParent();

        if (parent != null) {
            model.setParent(saveWithParents(parent));
        }

        return save(model);
    }

//    private CategoryTransactionModel getCategoryTransactionModel(ResultSet resultSet) throws SQLException {
//        CategoryTransactionModel categoryTransactionModel = null;
//        if (resultSet.next()) {
//            long id = resultSet.getLong("id");
//            String currentName = resultSet.getString("name");
//            long parentId = resultSet.getLong("parent");
//            Optional<CategoryTransactionModel> parent = findById(parentId);
//            categoryTransactionModel = new CategoryTransactionModel().setId(id).setName(currentName)
//                    .setParent(parent.orElse(null));
//        }
//        return categoryTransactionModel;
//    }
}