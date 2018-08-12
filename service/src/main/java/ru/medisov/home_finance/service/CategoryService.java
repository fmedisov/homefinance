package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService extends Service<CategoryTransactionModel> {
    Optional<CategoryTransactionModel> findByName(String name);

    Optional<CategoryTransactionModel> findById(Long aLong);

    Collection<CategoryTransactionModel> findAll();

    boolean remove(Long id);

    CategoryTransactionModel save(CategoryTransactionModel model);

    CategoryTransactionModel update(CategoryTransactionModel model);

    CategoryTransactionModel makeFromTextFields(String name, String parent);
}