package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.common.utils.MoneyUtils;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.dao.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountServiceImpl extends AbstractService implements AccountService {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AccountRepository repository;

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
    public Optional<AccountModel> findById(Long aLong) {
        try {
            Optional<AccountModel> optional = repository.findById(aLong);
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
        //new TransactionServiceImpl().removeByAccount(id);
        //repository.removeByAccount(id);
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

    @Override
    public AccountModel makeFromTextFields(String name, String currency, String accountType, String amount) {
        CurrencyModel currencyModel = currencyService.findByName(currency).orElseThrow(HomeFinanceServiceException::new);
        AccountType parsedType = AccountType.findByName(accountType).orElseThrow(HomeFinanceServiceException::new);
        BigDecimal parsedAmount = MoneyUtils.inBigDecimal(amount);
        return new AccountModel().setName(name).setCurrencyModel(currencyModel).setAccountType(parsedType).setAmount(parsedAmount);
    }
}
