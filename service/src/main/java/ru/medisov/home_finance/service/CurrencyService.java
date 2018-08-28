package ru.medisov.home_finance.service;

import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.CurrencyModel;

import java.util.Collection;
import java.util.Optional;

@Component
public interface CurrencyService extends Service<CurrencyModel> {
    Optional<CurrencyModel> findByName(String name);

    Optional<CurrencyModel> findById(Long aLong);

    Collection<CurrencyModel> findAll();

    boolean remove(Long id);

    CurrencyModel save(CurrencyModel model);

    CurrencyModel update(CurrencyModel model);
}