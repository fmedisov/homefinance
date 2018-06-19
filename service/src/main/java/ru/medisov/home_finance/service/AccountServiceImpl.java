package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.dao.repository.AccountRepository;
import ru.medisov.home_finance.dao.repository.Repository;

import java.util.Collection;
import java.util.Optional;

public class AccountServiceImpl extends AbstractService implements AccountService {
    private Repository<AccountModel, Long> repository = new AccountRepository();

    @Override
    public Optional<AccountModel> findByName(String name) {
        try {
            Optional<AccountModel> optional = repository.findByName(name);
            AccountModel model = optional.orElseThrow(HomeFinanceDaoException::new);
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
}
