package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.TransactionModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

//todo implement method for remove tags by transactions
//todo implement methods for save tags by transactions

@Component
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {

    @Query("select t from TransactionModel t where t.name = :name")
    Optional<TransactionModel> findByName(@Param("name") String name);

    @Query("SELECT t FROM TransactionModel t WHERE t.dateTime >= :dateFrom AND t.dateTime <= :upToDate")
    Collection<TransactionModel> findByPeriod(@Param("dateFrom") LocalDateTime dateFrom, @Param("upToDate") LocalDateTime upToDate);

    @Query("SELECT t FROM TransactionModel t WHERE t.category IS NULL OR t.category.id = :id")
    Collection<TransactionModel> findByCategory(@Param("id") Long id);

    @Query("SELECT t FROM TransactionModel t WHERE t.transactionType = 'Income' AND t.dateTime >= :dateFrom AND t.dateTime <= :upToDate")
    Collection<TransactionModel> incomeByPeriod(@Param("dateFrom") LocalDateTime dateFrom, @Param("upToDate")LocalDateTime upToDate);

    @Query("SELECT t FROM TransactionModel t WHERE t.transactionType = 'Expense' AND t.dateTime >= :dateFrom AND t.dateTime <= :upToDate")
    Collection<TransactionModel> expenseByPeriod(@Param("dateFrom") LocalDateTime dateFrom, @Param("upToDate") LocalDateTime upToDate);

    @Query("DELETE FROM TransactionModel t WHERE t.account = :accountId")
    boolean removeByAccount(@Param("accountId") Long accountId);
}