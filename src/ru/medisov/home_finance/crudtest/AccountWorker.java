package ru.medisov.home_finance.crudtest;

import ru.medisov.home_finance.dao.model.AccountModel;
import ru.medisov.home_finance.dao.model.AccountType;
import ru.medisov.home_finance.dao.repository.AccountRepository;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;
import ru.medisov.home_finance.dao.repository.Repository;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class AccountWorker {
    private AccountModel accountModel = new AccountModel("credit_card_Sberbank", AccountType.CREDIT_CARD,
            new CurrencyRepository().findById(1L).orElse(null), BigDecimal.valueOf(25000));
    private Repository<AccountModel, Long> repository = new AccountRepository();

    private static Logger logger = Logger.getLogger(AccountWorker.class.getName());

    public AccountWorker() {}

    public void crudTest() {
        addToDb();
        findInDb();
        findAll();
        update();
        deleteFromDb();
    }

    private void update() {
        accountModel.setName("Sber");
        repository.update(accountModel);
        findInDb();
    }

    private void deleteFromDb() {
        repository.remove(accountModel.getId());
    }

    private void findAll() {
        repository.findAll().forEach(c -> logger.info(c.toString()));
    }

    private void findInDb() {
        repository.findById(accountModel.getId()).ifPresent(c -> logger.info(c.toString()));
    }

    private void addToDb() {
        logger.info(accountModel.toString());
        accountModel = repository.save(accountModel);
        logger.info(accountModel.toString());
    }
}
