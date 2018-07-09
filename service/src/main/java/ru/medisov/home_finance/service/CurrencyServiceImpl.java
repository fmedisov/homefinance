package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;
import ru.medisov.home_finance.dao.repository.CurrencyRepositoryImpl;

import java.util.Collection;
import java.util.Optional;

public class CurrencyServiceImpl extends AbstractService implements CurrencyService {
    private CurrencyRepository repository = new CurrencyRepositoryImpl();

    @Override
    public Optional<CurrencyModel> findByName(String name) {
        try {
            Optional<CurrencyModel> optional = repository.findByName(name);
            CurrencyModel currencyModel = optional.orElseThrow(HomeFinanceDaoException::new);
            validate(currencyModel);
            return Optional.of(currencyModel);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CurrencyModel> findById(Long aLong) {
        try {
            Optional<CurrencyModel> optional = repository.findById(aLong);
            CurrencyModel currencyModel = optional.orElseThrow(HomeFinanceDaoException::new);
            validate(currencyModel);
            return Optional.of(currencyModel);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<CurrencyModel> findAll() {
        Collection<CurrencyModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        return repository.remove(id);
    }

    @Override
    public CurrencyModel save(CurrencyModel model) {
        currencyVerification(model);
        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public CurrencyModel update(CurrencyModel model) {
        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            newModel = repository.update(model);
        }

        return newModel;
    }

    private void currencyVerification(CurrencyModel model) throws HomeFinanceServiceException {
        String name = model.getName();
        repository.findByName(name).ifPresent(found -> {
            if (name.equals(found.getName())) {
                throw new HomeFinanceServiceException("Валюта уже существует");
            }
        });
    }
}
