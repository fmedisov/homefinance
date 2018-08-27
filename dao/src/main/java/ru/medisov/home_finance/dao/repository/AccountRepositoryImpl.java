package ru.medisov.home_finance.dao.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.common.model.AccountModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Optional;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class AccountRepositoryImpl extends AbstractRepository<AccountModel, Long> implements AccountRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AccountModel save(AccountModel model) {
        em.persist(model);
        return model;
    }

    @Override
    public Optional<AccountModel> findByName(String name) {
        Query query = em.createNamedQuery("AccountModel.findByName", AccountModel.class);
        query.setParameter("name", name);
        return Optional.ofNullable((AccountModel) query.getSingleResult());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<AccountModel> findAll() {
        return em.createNamedQuery("AccountModel.findAll", AccountModel.class).getResultList();
    }

    @Override
    public boolean remove(Long aLong) {
        em.remove(em.find(AccountModel.class, aLong));
        return true;
    }

    @Override
    public AccountModel update(AccountModel model) {
        return em.merge(model);
    }

    public Optional<AccountModel> findById(Long aLong) {
        return Optional.ofNullable(em.find(AccountModel.class, aLong));
    }
}