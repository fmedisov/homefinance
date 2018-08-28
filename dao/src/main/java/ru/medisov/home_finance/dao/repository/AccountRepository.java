package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.medisov.home_finance.common.model.AccountModel;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {

    @Query("select a from AccountModel a where a.name = :name")
    Optional<AccountModel> findByName(@Param("name") String name);
}