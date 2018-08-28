package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.medisov.home_finance.common.model.CurrencyModel;

import java.util.Optional;

@Component
@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyModel, Long> {

    @Query("select c from CurrencyModel c where c.name = :name")
    Optional<CurrencyModel> findByName(@Param("name") String name);
}