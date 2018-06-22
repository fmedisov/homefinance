package ru.medisov.home_finance.dao.repository;

import java.util.Optional;

public interface ExtendedRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findByName(String name);
}
