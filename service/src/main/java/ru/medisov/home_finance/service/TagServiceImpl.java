package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.repository.TagRepository;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.*;
import java.util.stream.Collectors;

//todo implement tests for tagService

@Component
@Service
public class TagServiceImpl extends CommonService implements TagService  {

    @Autowired
    private TagRepository repository;

    @Override
    public Optional<TagModel> findByName(String name) {
        try {
            Optional<TagModel> optional = repository.findByName(name);
            TagModel model = optional.orElseThrow(HomeFinanceServiceException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TagModel> findByNameAndCurrentUser(String name) {
        try {
            Optional<TagModel> optional = repository.findByNameAndUserModel(name, getCurrentUser());
            TagModel model = optional.orElseThrow(HomeFinanceServiceException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TagModel> findById(Long aLong) {
        try {
            Optional<TagModel> optional = repository.findById(aLong);
            TagModel model = optional.orElseThrow(HomeFinanceServiceException::new);
            validate(model);

            return Optional.of(model);
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<TagModel> findAll() {
        Collection<TagModel> models = repository.findAll();
        models.forEach(this::validate);

        return models;
    }

    @Override
    public Collection<TagModel> findAllByCurrentUser() {
        Collection<TagModel> models = repository.findAllByUserModel(getCurrentUser());
        models.forEach(this::validate);

        return models;
    }

    @Override
    public boolean remove(Long id) {
        boolean isExist = repository.existsById(id);
        repository.deleteById(id);
        return isExist;
    }

    @Override
    public TagModel save(TagModel model) {
        TagModel newModel = new TagModel();
        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public TagModel update(TagModel model) {
        TagModel newModel = new TagModel();

        if (validate(model)) {
            model.setUserModel(getCurrentUser());
            newModel = repository.saveAndFlush(model);
        }

        return newModel;
    }

    @Override
    public TagModel saveUpdate(TagModel model) {
        if (model.getId() == null) {
            return save(model);
        } else {
            return update(model);
        }
    }

    @Override
    public Set<TagModel> fromStringList(String allTags, String delimiter) {
        Set<TagModel> tagList = getValidTags(parseTags(Arrays.stream(allTags.split(delimiter)).collect(Collectors.toSet())));
        Set<TagModel> existAndNew = tagList.stream().map(t -> findByName(t.getName()).orElse(t)).collect(Collectors.toSet());
        Set<TagModel> existing = existAndNew.stream().filter(t -> t.getId() != null).collect(Collectors.toSet());
        Set<TagModel> saved = existAndNew.stream().filter(t -> t.getId() == null).collect(Collectors.toSet());

        Set<TagModel> result = new HashSet<>(existing);
        result.addAll(saved);

        return result;
    }

    @Override
    public Set<TagModel> fromStringListByCurrentUser(String allTags, String delimiter) {
        Set<TagModel> tagList = getValidTags(parseTags(Arrays.stream(allTags.split(delimiter)).collect(Collectors.toSet())));
        Set<TagModel> existAndNew = tagList.stream().map(t -> findByNameAndCurrentUser(t.getName()).orElse(t)).collect(Collectors.toSet());
        Set<TagModel> existing = existAndNew.stream().filter(t -> t.getId() != null).collect(Collectors.toSet());
        Set<TagModel> saved = existAndNew.stream().filter(t -> t.getId() == null).collect(Collectors.toSet());

        Set<TagModel> result = new HashSet<>(existing);
        result.addAll(saved);

        return result;
    }

    private Set<TagModel> parseTags(Set<String> allTags) {
        return allTags.stream().map(TagModel::new).collect(Collectors.toSet());
    }

    private Set<TagModel> getValidTags(Set<TagModel> models) {
        if (models == null) {
            return new HashSet<>();
        }

        return models.stream().filter(this::checkTag).collect(Collectors.toSet());
    }

    private boolean checkTag(TagModel model) {
        return model != null &&
                model.getName() != null &&
                model.getName().length() > 0;
    }
}