package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.common.model.TransactionModel;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TransactionRepository extends ExtendedRepository<TransactionModel, Long> {
    Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Collection<TransactionModel> findByCategory(Long id);

    Collection<TransactionModel> incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Collection<TransactionModel> expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    boolean removeByAccount(Long accountId);
}