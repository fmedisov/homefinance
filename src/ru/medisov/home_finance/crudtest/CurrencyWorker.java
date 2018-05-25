package ru.medisov.home_finance.crudtest;

import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;
import ru.medisov.home_finance.dao.repository.Repository;

import java.util.logging.Logger;

public class CurrencyWorker {
    private CurrencyModel currencyModel = new CurrencyModel("Боливиано", "BOB", "$");
    private Repository<CurrencyModel, Long> repository = new CurrencyRepository();

    private static Logger logger = Logger.getLogger(CurrencyWorker.class.getName());

    public CurrencyWorker() {}

    public void crudTest() {
        addToDb();
        findInDb();
        findAll();
        update();
        deleteFromDb();
    }

    private void update() {
        currencyModel.setName("Эскудо");
        currencyModel.setCode("CVE");
        repository.update(currencyModel);
        findInDb();
    }

    private void deleteFromDb() {
        repository.remove(currencyModel.getId());
    }

    private void findAll() {
        repository.findAll().forEach(c -> logger.info(c.toString()));
    }

    private void findInDb() {
        repository.findById(currencyModel.getId()).ifPresent(c -> logger.info(c.toString()));
    }

    private void addToDb() {
        logger.info(currencyModel.toString());
        currencyModel = repository.save(currencyModel);
        logger.info(currencyModel.toString());
    }
}
