package ru.medisov.home_finance.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.common.model.TransactionModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;


@Transactional
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionRepositoryImpl extends AbstractRepository<TransactionModel, Long> implements TransactionRepository {

    @Autowired
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public TransactionModel save(TransactionModel model) {
        em.persist(model);
        return model;
    }

    @Override
    public Optional<TransactionModel> findByName(String name) {
        try {
            Query query = em.createNamedQuery("TransactionModel.findByName", TransactionModel.class);
            query.setParameter("name", name);
            return Optional.ofNullable((TransactionModel) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TransactionModel> findById(Long aLong) {
        return Optional.ofNullable(em.find(TransactionModel.class, aLong));
    }

    @Override
    public Collection<TransactionModel> findAll() {
        return em.createNamedQuery("TransactionModel.findAll", TransactionModel.class).getResultList();
    }

    @Override
    public boolean remove(Long aLong) {
        tagRepository.removeByTransaction(aLong);
        em.remove(em.find(TransactionModel.class, aLong));
        return true;
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        return em.merge(model);
    }

    @Override
    public Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return em.createNamedQuery("TransactionModel.findByPeriod", TransactionModel.class)
                .setParameter("dateFrom", dateFrom)
                .setParameter("upToDate", upToDate)
                .getResultList();
    }

    @Override
    public Collection<TransactionModel> findByCategory(Long categoryId) {
        return em.createNamedQuery("TransactionModel.findByCategory", TransactionModel.class)
                .setParameter("category", categoryId)
                .getResultList();
    }

    @Override
    public Collection<TransactionModel> incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return em.createNamedQuery("TransactionModel.incomeByPeriod", TransactionModel.class)
                .setParameter("dateFrom", dateFrom)
                .setParameter("upToDate", upToDate)
                .getResultList();
    }

    @Override
    public Collection<TransactionModel> expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate) {
        return em.createNamedQuery("TransactionModel.expenseByPeriod", TransactionModel.class)
                .setParameter("dateFrom", dateFrom)
                .setParameter("upToDate", upToDate)
                .getResultList();
    }

    @Override
    public boolean removeByAccount(Long accountId) {
        return false;
    }
}