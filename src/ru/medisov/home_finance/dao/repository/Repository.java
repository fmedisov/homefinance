package ru.medisov.home_finance.dao.repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Generic CRUD
 */
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    Collection<T> findAll();

    boolean remove(ID id);

    T save(T model);

    T update(T model);
}