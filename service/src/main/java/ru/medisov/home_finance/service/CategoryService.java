package ru.medisov.home_finance.service;

import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.util.Collection;
import java.util.Optional;

@Component
public interface CategoryService extends Service<CategoryTransactionModel> {
    Optional<CategoryTransactionModel> findByName(String name);

    Optional<CategoryTransactionModel> findById(Long aLong);

    Collection<CategoryTransactionModel> findAll();

    boolean remove(Long id);

    CategoryTransactionModel save(CategoryTransactionModel model);

    CategoryTransactionModel update(CategoryTransactionModel model);
}