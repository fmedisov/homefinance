package ru.medisov.home_finance.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.medisov.home_finance.common.model.TagModel;

import java.util.*;

@Component
@Transactional
public interface TagRepository extends JpaRepository<TagModel, Long> {

    @Query("select t from TagModel t where t.name = :name")
    Optional<TagModel> findByName(@Param("name") String name);
}
