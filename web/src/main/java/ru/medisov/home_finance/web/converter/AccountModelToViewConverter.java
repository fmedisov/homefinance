package ru.medisov.home_finance.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.web.view.AccountView;

public class AccountModelToViewConverter implements Converter<AccountModel, AccountView> {

    //todo rename converter classes and methods
    public AccountView convert(AccountModel accountModel) {
        AccountView accountView = new AccountView();
        accountView
                .setId(accountModel.getId())
                .setName(accountModel.getName())
                .setAccountType(accountModel.getAccountType().getName())
                .setCurrencyModel(accountModel.getCurrencyModel().getName())
                .setAmount(accountModel.getAmount());

        return accountView;
    }

}