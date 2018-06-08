package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;

import java.util.Collection;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {
    @Override
    public Optional<CurrencyModel> findByName(String name) {
        return new CurrencyRepository().findByName(name);
    }

    @Override
    public Collection<CurrencyModel> findAll() {
        return new CurrencyRepository().findAll();
    }

    @Override
    public boolean remove(Long id) {
        return new CurrencyRepository().remove(id);
    }

    @Override
    public CurrencyModel save(CurrencyModel model) {
        return new CurrencyRepository().save(model);
    }

    @Override
    public CurrencyModel update(CurrencyModel model) {
        return new CurrencyRepository().update(model);
    }
}
