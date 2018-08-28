package ru.medisov.home_finance.dao.repository;

import org.springframework.stereotype.Component;

import java.util.Collection;
        import java.util.Optional;

/**
 * Generic CRUD
 */
@Component
public interface Repository<T, ID> {

    Optional<T> findById(Long aLong);

    Optional<T> findByName(String name);

    Collection<T> findAll();

    boolean remove(ID id);

    T save(T model);

    T update(T model);
}