package ru.medisov.home_finance.dao.repository;

import java.util.Collection;
        import java.util.Optional;

/**
 * Generic CRUD
 */
//todo implement different interfaces
public interface Repository<T, ID> {
    //todo implement interface for this method
    Optional<T> findByName(String name);

    Collection<T> findAll();

    boolean remove(ID id);

    T save(T model);

    T update(T model);
}