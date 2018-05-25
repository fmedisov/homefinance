package ru.medisov.home_finance.crudtest;

import ru.medisov.home_finance.dao.model.TransactionModel;
import ru.medisov.home_finance.dao.model.TransactionType;
import ru.medisov.home_finance.dao.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Logger;

public class TransactionWorker {
    private TransactionModel transactionModel = new TransactionModel(
            BigDecimal.valueOf(300),
            LocalDateTime.now(),
            //todo заменить код с get для optional
            new AccountRepository().findById(2L).get(),
            new CategoryRepository().findById(3L).get(),
            TransactionType.EXPENSE,
            //todo найти способ правильно записать теги в базу
            Arrays.asList("#метро,#проезд,#транспорт".split(","))
    );
    private Repository<TransactionModel, Long> repository = new TransactionRepository();

    private static Logger logger = Logger.getLogger(TransactionWorker.class.getName());

    public TransactionWorker() {}

    //todo реализовать интерфейс для удобной работы с репозиториями
    public void crudTest() {

        addToDb();
        findInDb();
        findAll();
        update();
        deleteFromDb();
    }

    private void update() {
        transactionModel.setDateTime(LocalDateTime.now());
        repository.update(transactionModel);
        findInDb();
    }

    private void deleteFromDb() {
        repository.remove(transactionModel.getId());
    }

    private void findAll() {
        repository.findAll().forEach(c -> logger.info(c.toString()));
    }

    private void findInDb() {
        repository.findById(transactionModel.getId()).ifPresent(c -> logger.info(c.toString()));
    }

    private void addToDb() {
        logger.info(transactionModel.toString());
        transactionModel = repository.save(transactionModel);
        logger.info(transactionModel.toString());
    }
}
