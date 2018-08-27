package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends AbstractService implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public Optional<CategoryTransactionModel> findByName(String name) {
        try {
            Optional<CategoryTransactionModel> optional = repository.findByName(name);
            CategoryTransactionModel model = optional.orElseThrow(HomeFinanceDaoException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CategoryTransactionModel> findById(Long aLong) {
        try {
            Optional<CategoryTransactionModel> optional = repository.findById(aLong);
            CategoryTransactionModel model = optional.orElseThrow(HomeFinanceDaoException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
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
}
