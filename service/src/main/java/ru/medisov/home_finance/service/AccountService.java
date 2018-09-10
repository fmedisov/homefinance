package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.AccountModel;

import java.util.Collection;
import java.util.Optional;

public interface AccountService extends Service<AccountModel> {
    Optional<AccountModel> findByName(String name);

    Optional<AccountModel> findByNameAndCurrentUser(String name);

    Optional<AccountModel> findById(Long aLong);

    Collection<AccountModel> findAll();

    Collection<AccountModel> findAllByCurrentUser();

    boolean remove(Long id);

    AccountModel save(AccountModel model);

    AccountModel update(AccountModel model);

}
