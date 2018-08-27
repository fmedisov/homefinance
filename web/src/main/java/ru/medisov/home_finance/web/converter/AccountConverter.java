package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.web.view.AccountView;

public class AccountConverter {

    private final CurrencyService currencyService;

    @Autowired
    public AccountConverter(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public AccountView toAccountView(AccountModel accountModel) {
        AccountView accountView = new AccountView();
        accountView
                .setId(accountModel.getId())
                .setName(accountModel.getName())
                .setAccountType(accountModel.getAccountType().getName())
                .setCurrencyModel(accountModel.getCurrencyModel().getName())
                .setAmount(accountModel.getAmount());

        return accountView;
    }

    public AccountModel toAccountModel(AccountView accountView) {
        AccountModel accountModel = new AccountModel();
        accountModel
                .setId(accountView.getId())
                .setName(accountView.getName())
                .setAccountType(AccountType.findByName(accountView.getAccountType()).orElse(null))
                .setCurrencyModel(currencyService.findByName(accountView.getCurrencyModel()).orElse(new CurrencyModel()))
                .setAmount(accountView.getAmount());

        return accountModel;
    }
}
