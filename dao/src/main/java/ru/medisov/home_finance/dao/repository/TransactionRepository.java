package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.common.model.TransactionModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

public interface TransactionRepository extends ExtendedRepository<TransactionModel, Long> {
    Collection<TransactionModel> findByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Collection<TransactionModel> findByCategory(long id);

    BigDecimal incomeByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    BigDecimal expenseByPeriod(LocalDateTime dateFrom, LocalDateTime upToDate);

    Map<String,BigDecimal> incomeByCategory(LocalDateTime dateFrom, LocalDateTime upToDate);

    Map<String,BigDecimal> expenseByCategory(LocalDateTime dateFrom, LocalDateTime upToDate);

    boolean removeByAccount(Long accountId);
}