package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.Optional;

@Component
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("select u from UserModel u where u.login = :login")
    Optional<UserModel> findByName(@Param("login") String login);
}
