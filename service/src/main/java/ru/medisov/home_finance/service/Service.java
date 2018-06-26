package ru.medisov.home_finance.service;

import java.util.Collection;
import java.util.Optional;

public interface Service<T> {
    Optional<T> findByName(String name);

    Collection<T> findAll();

    boolean remove(Long id);

    T save(T model);

    T update(T model);
}