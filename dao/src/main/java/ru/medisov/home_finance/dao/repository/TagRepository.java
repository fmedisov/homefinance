package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.*;

@Component
@Transactional
public interface TagRepository extends JpaRepository<TagModel, Long> {

    Optional<TagModel> findByName(@Param("name") String name);

    Optional<TagModel> findByNameAndUserModel(@Param("name") String name, @Param("userModel") UserModel userModel);

    Collection<TagModel> findAllByUserModel(@Param("userModel") UserModel userModel);
}
