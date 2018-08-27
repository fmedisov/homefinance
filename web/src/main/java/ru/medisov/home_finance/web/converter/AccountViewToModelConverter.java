package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.web.view.AccountView;

public class AccountViewToModelConverter implements Converter<AccountView, AccountModel> {

    private final CurrencyService currencyService;

    @Autowired
    //todo implement without constructor
    public AccountViewToModelConverter(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public AccountModel convert(AccountView accountView) {
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