package ru.medisov.home_finance.service;

import java.util.Collection;
import java.util.Optional;

public interface Service<T> {
    Optional<T> findByName(String name);

    // todo implement tests for this method
    Optional<T> findById(Long aLong);

    Collection<T> findAll();

    boolean remove(Long id);

    T save(T model);

    T update(T model);

    //todo implement tests for method
    T saveUpdate(T model);
}