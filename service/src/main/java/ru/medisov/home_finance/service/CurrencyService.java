package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.CurrencyModel;

import java.util.Collection;
import java.util.Optional;

public interface CurrencyService extends Service<CurrencyModel> {
    Optional<CurrencyModel> findByName(String name);

    Optional<CurrencyModel> findByNameAndCurrentUser(String name);

    Optional<CurrencyModel> findById(Long aLong);

    Collection<CurrencyModel> findAll();

    Collection<CurrencyModel> findAllByCurrentUser();

    boolean remove(Long id);

    CurrencyModel save(CurrencyModel model);

    CurrencyModel saveByCurrentUser(CurrencyModel model);

    CurrencyModel update(CurrencyModel model);

    CurrencyModel saveUpdateByCurrentUser(CurrencyModel model);
}