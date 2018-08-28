package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryTransactionModel, Long> {

    @Query("select c from CategoryTransactionModel c where c.name = :name")
    Optional<CategoryTransactionModel> findByName(@Param("name") String name);

}