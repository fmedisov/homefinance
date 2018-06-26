package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TransactionModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface TransactionService extends Service<TransactionModel> {
    Optional<TransactionModel> findByName(String name);

    Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Collection<TransactionModel> findAll();

    boolean remove(Long id);

    TransactionModel save(TransactionModel model);

    TransactionModel update(TransactionModel model);

    Collection<TransactionModel> findByCategory(CategoryTransactionModel category);

    Map<String, IncomeExpense> sumByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate, Class<CategoryTransactionModel> oClass);
}
