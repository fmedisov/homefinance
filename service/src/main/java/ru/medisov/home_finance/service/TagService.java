package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.TagModel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagService extends Service<TagModel> {
    Optional<TagModel> findByName(String name);

    Optional<TagModel> findById(Long aLong);

    Collection<TagModel> findAll();

    boolean remove(Long id);

    TagModel save(TagModel model);

    TagModel update(TagModel model);

    Collection<TagModel> findByNames(List<TagModel> models);

    List<TagModel> saveTagList(List<TagModel> models);

    List<TagModel> updateTagList(List<TagModel> models);

    List<TagModel> saveUpdateByTransaction(List<TagModel> allTags, Long transactionId);

    List<TagModel> saveUpdateFromStringList(List<String> allTags, Long transactionId);

    List<TagModel> fromStringList(String allTags, String delimiter);

    List<TagModel> findByTransaction(Long transactionId);

    boolean removeByTransaction(Long transactionId);
}