package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface ExtendedRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findByName(String name);
}
