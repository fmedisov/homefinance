package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.Collection;
import java.util.Optional;

@Component
@Service
public class CategoryServiceImpl extends CommonService implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public Optional<CategoryTransactionModel> findByName(String name) {
        try {
            Optional<CategoryTransactionModel> optional = repository.findByName(name);
            CategoryTransactionModel model = optional.orElseThrow(HomeFinanceServiceException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CategoryTransactionModel> findByNameAndCurrentUser(String name) {
        try {
            Optional<CategoryTransactionModel> optional = repository.findByNameAndUserModel(name, getCurrentUser());
            CategoryTransactionModel model = optional.orElseThrow(HomeFinanceServiceException::new);
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
            CategoryTransactionModel model = optional.orElseThrow(HomeFinanceServiceException::new);
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
        Collection<CategoryTransactionModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<CategoryTransactionModel> findAllByCurrentUser() {
        Collection<CategoryTransactionModel> models = repository.findAllByUserModel(getCurrentUser());
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        boolean isExist = repository.existsById(id);
        repository.deleteById(id);
        return isExist;
    }

    @Override
    public CategoryTransactionModel save(CategoryTransactionModel model) {
        CategoryTransactionModel newModel = new CategoryTransactionModel();
        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public CategoryTransactionModel update(CategoryTransactionModel model) {
        CategoryTransactionModel newModel = new CategoryTransactionModel();

        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.saveAndFlush(model);
        }

        return newModel;
    }

    @Override
    public CategoryTransactionModel saveUpdate(CategoryTransactionModel model) {
        if (model.getId() == null) {
            return save(model);
        } else {
            return update(model);
        }
    }
}
