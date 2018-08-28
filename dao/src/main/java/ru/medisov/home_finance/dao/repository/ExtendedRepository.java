package ru.medisov.home_finance.dao.repository;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ExtendedRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findByName(String name);
}
