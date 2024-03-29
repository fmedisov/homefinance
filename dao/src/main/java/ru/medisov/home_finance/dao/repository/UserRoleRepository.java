package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.medisov.home_finance.common.model.UserRoleModel;

import java.util.List;

@Component
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleModel, Long> {

    List<UserRoleModel> findAllByUserModel_UserId(@Param("userId") Long userId);

}
