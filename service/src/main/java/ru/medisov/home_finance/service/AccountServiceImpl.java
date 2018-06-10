package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.AccountModel;
import ru.medisov.home_finance.dao.repository.AccountRepository;
import ru.medisov.home_finance.dao.repository.Repository;
import ru.medisov.home_finance.dao.validator.ClassValidator;
import ru.medisov.home_finance.dao.validator.Validator;

import java.util.Collection;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    private Validator validator = new ClassValidator();
    private Repository<AccountModel, Long> repository = new AccountRepository();

    @Override
    public Optional<AccountModel> findByName(String name) {
        try {
            Optional<AccountModel> optional = repository.findByName(name);
            AccountModel model = optional.orElseGet(AccountModel::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Collection<AccountModel> findAll() {
        Collection<AccountModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        new TransactionServiceImpl().removeByAccount(id);
        return repository.remove(id);
    }

    @Override
    public AccountModel save(AccountModel model) {
        AccountModel newModel = new AccountModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }
        return newModel;
    }

    @Override
    public AccountModel update(AccountModel model) {
        AccountModel newModel = new AccountModel();
        if (validate(model)) {
            newModel = repository.update(model);
        }
        return newModel;
    }

    private boolean validate(AccountModel model) {
        try {
            if (!validator.isValid(model)) {
                throw new HomeFinanceServiceException("Кошелек " + model + " не валидирован");
            }
        } catch (HomeFinanceServiceException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
