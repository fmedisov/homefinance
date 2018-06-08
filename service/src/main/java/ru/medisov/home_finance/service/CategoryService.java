package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.CategoryTransactionModel;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {
    Optional<CategoryTransactionModel> findByName(String name);

    Collection<CategoryTransactionModel> findAll();

    boolean remove(Long id);

    CategoryTransactionModel save(CategoryTransactionModel model);

    CategoryTransactionModel update(CategoryTransactionModel model);
}