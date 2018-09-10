package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.Collection;
import java.util.Optional;

@Component
@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyModel, Long> {

    Optional<CurrencyModel> findByName(@Param("name") String name);

    Optional<CurrencyModel> findByNameAndUserModel(@Param("name") String name, @Param("userModel") UserModel userModel);

    Collection<CurrencyModel> findAllByUserModel(@Param("userModel") UserModel userModel);
}