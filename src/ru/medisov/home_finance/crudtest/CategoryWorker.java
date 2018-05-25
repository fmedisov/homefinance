package ru.medisov.home_finance.crudtest;

import ru.medisov.home_finance.dao.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;
import ru.medisov.home_finance.dao.repository.Repository;

import java.util.logging.Logger;

public class CategoryWorker {
    private CategoryTransactionModel categoryModel = new CategoryTransactionModel("Отдых", null);
    private Repository<CategoryTransactionModel, Long> repository = new CategoryRepository();

    private static Logger logger = Logger.getLogger(CategoryWorker.class.getName());

    public CategoryWorker() {}

    public void crudTest() {
        addToDb();
        findInDb();
        findAll();
        update();
        deleteFromDb();
    }

    private void update() {
        categoryModel.setName("Бухло");
        repository.update(categoryModel);
        findInDb();
    }

    private void deleteFromDb() {
        repository.remove(categoryModel.getId());
    }

    private void findAll() {
        repository.findAll().forEach(c -> logger.info(c.toString()));
    }

    private void findInDb() {
        repository.findById(categoryModel.getId()).ifPresent(c -> logger.info(c.toString()));
    }

    private void addToDb() {
        logger.info(categoryModel.toString());
        categoryModel = repository.save(categoryModel);
        logger.info(categoryModel.toString());
    }
}
