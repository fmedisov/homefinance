package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;

import java.util.Collection;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public Optional<CategoryTransactionModel> findByName(String name) {
        return new CategoryRepository().findByName(name);
    }

    @Override
    public Collection<CategoryTransactionModel> findAll() {
        return new CategoryRepository().findAll();
    }

    @Override
    public boolean remove(Long id) {
        return new CategoryRepository().remove(id);
    }

    @Override
    public CategoryTransactionModel save(CategoryTransactionModel model) {
        return new CategoryRepository().save(model);
    }

    @Override
    public CategoryTransactionModel update(CategoryTransactionModel model) {
        return new CategoryRepository().update(model);
    }
}
