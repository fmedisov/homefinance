package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.common.model.CategoryTransactionModel;

public interface CategoryRepository extends ExtendedRepository<CategoryTransactionModel, Long> {

    CategoryTransactionModel saveWithParents(CategoryTransactionModel model);

}