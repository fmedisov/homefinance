package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;
import ru.medisov.home_finance.dao.repository.Repository;
import ru.medisov.home_finance.dao.validator.ClassValidator;
import ru.medisov.home_finance.dao.validator.Validator;

import java.util.Collection;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {
    private Validator validator = new ClassValidator();
    private Repository<CategoryTransactionModel, Long> repository = new CategoryRepository();

    @Override
    public Optional<CategoryTransactionModel> findByName(String name) {
        try {
            Optional<CategoryTransactionModel> optional = repository.findByName(name);
            CategoryTransactionModel model = optional.orElseGet(CategoryTransactionModel::new);
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
    public Collection<CategoryTransactionModel> findAll() {
        //todo как именно валидировать
        Collection<CategoryTransactionModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        return repository.remove(id);
    }

    @Override
    public CategoryTransactionModel save(CategoryTransactionModel model) {
        CategoryTransactionModel newModel = new CategoryTransactionModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }
        return newModel;
    }

    @Override
    public CategoryTransactionModel update(CategoryTransactionModel model) {
        CategoryTransactionModel newModel = new CategoryTransactionModel();
        if (validate(model)) {
            newModel = repository.update(model);
        }
        return newModel;
    }

    //todo remove duplicate code
    private boolean validate(CategoryTransactionModel model) {
        try {
            if (!validator.isValid(model)) {
                throw new HomeFinanceServiceException("Категория " + model + " не валидирована");
            }
        } catch (HomeFinanceServiceException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
