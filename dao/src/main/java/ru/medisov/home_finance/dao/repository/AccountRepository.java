package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {

    Optional<AccountModel> findByName(@Param("name") String name);

    Optional<AccountModel> findByNameAndUserModel(@Param("name") String name, @Param("userModel") UserModel userModel);

    Collection<AccountModel> findAllByUserModel(@Param("userModel") UserModel userModel);
}