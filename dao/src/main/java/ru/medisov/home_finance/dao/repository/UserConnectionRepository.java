package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.medisov.home_finance.common.model.UserConnection;

import java.util.Optional;

@Component
@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

    Optional<UserConnection> findByProviderUserId(@Param("providerUserId") String providerUserId);

}
