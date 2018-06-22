package ru.medisov.home_finance.dao.repository;

import ru.medisov.home_finance.common.model.TagModel;

import java.util.List;

public interface TagRepository extends ExtendedRepository<TagModel, Long> {

    List<TagModel> saveByTransaction(List<TagModel> tags, Long transactionId);

    List<TagModel> findByTransaction(Long transactionId);

    boolean removeByTransaction(Long transactionId);
}
