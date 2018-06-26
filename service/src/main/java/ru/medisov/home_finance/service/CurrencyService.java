package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.CurrencyModel;

import java.util.Collection;
import java.util.Optional;

public interface CurrencyService extends Service<CurrencyModel> {
    Optional<CurrencyModel> findByName(String name);

    Collection<CurrencyModel> findAll();

    boolean remove(Long id);

    CurrencyModel save(CurrencyModel model);

    CurrencyModel update(CurrencyModel model);
}