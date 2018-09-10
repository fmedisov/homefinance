package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.Collection;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryTransactionModel, Long> {

    Optional<CategoryTransactionModel> findByName(@Param("name") String name);

    Optional<CategoryTransactionModel> findByNameAndUserModel(@Param("name") String name, @Param("userModel") UserModel userModel);

    Collection<CategoryTransactionModel> findAllByUserModel(@Param("userModel") UserModel userModel);
}