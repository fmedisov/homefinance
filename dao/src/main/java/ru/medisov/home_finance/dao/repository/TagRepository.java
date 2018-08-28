package ru.medisov.home_finance.dao.repository;

import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.TagModel;

import java.util.Collection;
import java.util.List;

@Component
public interface TagRepository extends ExtendedRepository<TagModel, Long> {

    List<TagModel> saveUpdateByTransaction(List<TagModel> tags, Long transactionId);

    List<TagModel> findByTransaction(Long transactionId);

    boolean removeByTransaction(Long transactionId);

    List<TagModel> saveTagList(List<TagModel> models);

    List<TagModel> updateTagList(List<TagModel> models);

    Collection<TagModel> findByNames(List<TagModel> models);
}
