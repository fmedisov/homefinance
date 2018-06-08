package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.AccountModel;
import ru.medisov.home_finance.dao.repository.AccountRepository;

import java.util.Collection;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    @Override
    public Optional<AccountModel> findByName(String name) {
        return new AccountRepository().findByName(name);
    }

    @Override
    public Collection<AccountModel> findAll() {
        return new AccountRepository().findAll();
    }

    @Override
    public boolean remove(Long id) {
        return new AccountRepository().remove(id);
    }

    @Override
    public AccountModel save(AccountModel model) {
        return new AccountRepository().save(model);
    }

    @Override
    public AccountModel update(AccountModel model) {
        return new AccountRepository().update(model);
    }
}
