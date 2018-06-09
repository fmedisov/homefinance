package ru.medisov.home_finance.service;

import java.util.Collection;
import java.util.Optional;

public interface CurrencyService {
    Optional<CurrencyModelDto> findByName(String name);

    Collection<CurrencyModelDto> findAll();

    boolean remove(Long id);

    CurrencyModelDto save(CurrencyModelDto model);

    CurrencyModelDto update(CurrencyModelDto model);
}