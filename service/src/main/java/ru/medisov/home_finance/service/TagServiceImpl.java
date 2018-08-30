package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.repository.TagRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
public class TagServiceImpl extends AbstractService implements TagService {

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
    public Optional<TagModel> findById(Long aLong) {
        try {
            Optional<TagModel> optional = repository.findById(aLong);
            TagModel model = optional.orElseThrow(HomeFinanceDaoException::new);
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
    public boolean remove(Long id) {
        repository.remove(id);
        return true;
    }

    @Override
    public TagModel save(TagModel model) {
        TagModel newModel = new TagModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }

        return newModel;
    }

    @Override
    public TagModel update(TagModel model) {
        TagModel newModel = new TagModel();

        if (validate(model)) {
            newModel = repository.update(model);
        }

        return newModel;
    }

    @Override
    public Collection<TagModel> findByNames(List<TagModel> models) {
        return repository.findByNames(models);
    }

    @Override
    public List<TagModel> saveTagList(List<TagModel> models) {
        return repository.saveTagList(getValidTags(models));
    }

    @Override
    public List<TagModel> updateTagList(List<TagModel> models) {
        return repository.updateTagList(getValidTags(models));
    }

    @Override
    public List<TagModel> saveUpdateByTransaction(List<TagModel> allTags, Long transactionId) {
        List<TagModel> validTags = getValidTags(allTags);

        if (validTags == null || Objects.equals(validTags, new ArrayList<>())) {
            repository.removeByTransaction(transactionId);
            return validTags;
        }

        return repository.saveUpdateByTransaction(validTags, transactionId);
    }

    @Override
    public List<TagModel> findByTransaction(Long transactionId) {
        return repository.findByTransaction(transactionId);
    }

    @Override
    public boolean removeByTransaction(Long transactionId) {
        return repository.removeByTransaction(transactionId);
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
    public List<TagModel> saveUpdateFromStringList(List<String> allTags, Long transactionId) {
        List<TagModel> tags = parseTags(allTags);
        return saveUpdateByTransaction(tags, transactionId);
    }

    @Override
    public List<TagModel> fromStringList(String allTags, String delimiter) {
        List<String> tagList = Arrays.asList(allTags.split(delimiter));
        return parseTags(tagList);
    }

    private List<TagModel> parseTags(List<String> allTags) {
        return allTags.stream().map(TagModel::new).collect(Collectors.toList());
    }

    private List<TagModel> getValidTags(List<TagModel> models) {
        if (models == null) {
            return new ArrayList<>();
        }

        return models.stream().filter(this::checkTag).collect(Collectors.toList());
    }

    private boolean checkTag(TagModel model) {
        return model != null &&
                model.getName() != null &&
                model.getName().length() > 0;
    }
}
