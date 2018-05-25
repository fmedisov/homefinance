package ru.medisov.home_finance;

import ru.medisov.home_finance.config.GlobalConfig;
import ru.medisov.home_finance.crudtest.AccountWorker;
import ru.medisov.home_finance.crudtest.CategoryWorker;
import ru.medisov.home_finance.crudtest.CurrencyWorker;
import ru.medisov.home_finance.crudtest.TransactionWorker;
import ru.medisov.home_finance.dao.model.CurrencyModel;

public class Runner {
    public static void main(String[] args) {

        GlobalConfig.initGlobalConfig();

        new CurrencyWorker().crudTest();
        new CategoryWorker().crudTest();
        new AccountWorker().crudTest();
        new TransactionWorker().crudTest();
    }
}