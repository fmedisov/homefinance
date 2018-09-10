package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.model.TransactionType;
import ru.medisov.home_finance.common.model.UserModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

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

    Collection<TransactionModel> findAllByUserModel(@Param("userModel") UserModel userModel);

    Optional<TransactionModel> findByNameAndUserModel(@Param("name") String name, @Param("userModel") UserModel userModel);

    Collection<TransactionModel> findByDateTimeBetweenAndUserModel(@Param("dateFrom") LocalDateTime dateFrom,
                                                                   @Param("upToDate") LocalDateTime upToDate,
                                                                   @Param("userModel") UserModel userModel);

    Collection<TransactionModel> findAllByTransactionTypeAndDateTimeBetweenAndUserModel(
            @Param("transactionType") TransactionType transactionType,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("upToDate") LocalDateTime upToDate,
            @Param("userModel") UserModel userModel
    );
}