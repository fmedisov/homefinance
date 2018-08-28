package ru.medisov.home_finance.dao.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.common.model.CurrencyModel;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;

@Component
@Transactional
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrencyRepositoryImpl implements CurrencyRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CurrencyModel save(CurrencyModel model) {
        em.persist(model);
        return model;
    }

    @Override
    public Optional<CurrencyModel> findByName(String name) {
        try {
            Query query = em.createNamedQuery("CurrencyModel.findByName", CurrencyModel.class);
            query.setParameter("name", name);
            return Optional.ofNullable((CurrencyModel) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<CurrencyModel> findAll() {
        return em.createNamedQuery("CurrencyModel.findAll", CurrencyModel.class).getResultList();
    }

    @Override
    public boolean remove(Long aLong) {
        em.remove(em.find(CurrencyModel.class, aLong));
        return true;
    }

    @Override
    public CurrencyModel update(CurrencyModel model) {
        return em.merge(model);
    }

    @Override
    public Optional<CurrencyModel> findById(Long aLong) {
        return Optional.ofNullable(em.find(CurrencyModel.class, aLong));
    }
}
