package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.TagModel;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface TagService extends Service<TagModel> {
    Optional<TagModel> findByName(String name);

    Optional<TagModel> findById(Long aLong);

    Collection<TagModel> findAll();

    boolean remove(Long id);

    TagModel save(TagModel model);

    TagModel update(TagModel model);

    Set<TagModel> fromStringList(String allTags, String delimiter);
}