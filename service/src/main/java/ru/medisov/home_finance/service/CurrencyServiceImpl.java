package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.Collection;
import java.util.Optional;

@Component
@Service
public class CurrencyServiceImpl extends CommonService implements CurrencyService {

    @Autowired
    private CurrencyRepository repository;

    @Override
    public Optional<CurrencyModel> findByName(String name) {
        try {
            Optional<CurrencyModel> optional = repository.findByName(name);
            CurrencyModel currencyModel = optional.orElseThrow(HomeFinanceServiceException::new);
            validate(currencyModel);
            return Optional.of(currencyModel);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CurrencyModel> findByNameAndCurrentUser(String name) {
        try {
            Optional<CurrencyModel> optional = repository.findByNameAndUserModel(name, getCurrentUser());
            CurrencyModel currencyModel = optional.orElseThrow(HomeFinanceServiceException::new);
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
            CurrencyModel currencyModel = optional.orElseThrow(HomeFinanceServiceException::new);
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
    public Collection<CurrencyModel> findAllByCurrentUser() {
        Collection<CurrencyModel> models = repository.findAllByUserModel(getCurrentUser());
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        boolean isExist = repository.existsById(id);
        repository.deleteById(id);
        return isExist;
    }

    @Override
    public CurrencyModel save(CurrencyModel model) {
        currencyVerification(model);
        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public CurrencyModel saveByCurrentUser(CurrencyModel model) {
        currencyVerificationByCurrentUser(model);
        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public CurrencyModel update(CurrencyModel model) {
        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.saveAndFlush(model);
        }

        return newModel;
    }

    @Override
    public CurrencyModel saveUpdate(CurrencyModel model) {
        if (model.getId() == null) {
            return save(model);
        } else {
            return update(model);
        }
    }

    @Override
    public CurrencyModel saveUpdateByCurrentUser(CurrencyModel model) {
        if (model.getId() == null) {
            return saveByCurrentUser(model);
        } else {
            return update(model);
        }
    }

    private void currencyVerification(CurrencyModel model) throws HomeFinanceServiceException {
        String name = model.getName();
        repository.findByName(name).ifPresent(found -> {
            if (name.equals(found.getName())) {
                throw new HomeFinanceServiceException("Валюта уже существует");
            }
        });
    }

    private void currencyVerificationByCurrentUser(CurrencyModel model) {
        String name = model.getName();
        repository.findByNameAndUserModel(name, getCurrentUser()).ifPresent(found -> {
            if (name.equals(found.getName())) {
                throw new HomeFinanceServiceException("Валюта уже существует");
            }
        });
    }
}
