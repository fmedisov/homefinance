package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TransactionModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface TransactionService extends Service<TransactionModel> {
    Optional<TransactionModel> findByName(String name);

    Optional<TransactionModel> findById(Long aLong);

    Collection<TransactionModel> findAll();

    boolean remove(Long id);

    TransactionModel save(TransactionModel model);

    TransactionModel update(TransactionModel model);

    Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Collection<TransactionModel> findByCategory(CategoryTransactionModel category);

    Collection<TransactionModel> incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Collection<TransactionModel> expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Map<String, IncomeExpense> sumByPeriodNoCategories(LocalDateTime dateFrom, LocalDateTime upToDate);

    Map<CategoryTransactionModel, IncomeExpense> sumByPeriodByCategories(LocalDateTime dateFrom, LocalDateTime upToDate);
}
