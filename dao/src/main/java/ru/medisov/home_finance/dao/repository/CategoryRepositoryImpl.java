package ru.medisov.home_finance.dao.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Optional;

@Component
@Transactional
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CategoryRepositoryImpl extends AbstractRepository<CategoryTransactionModel, Long> implements CategoryRepository {

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
}