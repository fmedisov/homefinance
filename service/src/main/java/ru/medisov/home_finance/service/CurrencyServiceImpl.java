package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;
import ru.medisov.home_finance.dao.repository.Repository;
import ru.medisov.home_finance.dao.validator.ClassValidator;
import ru.medisov.home_finance.dao.validator.Validator;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrencyServiceImpl implements CurrencyService {
    private Validator validator = new ClassValidator();
    private Repository<CurrencyModel, Long> repository;

    public CurrencyServiceImpl() {
        repository = new CurrencyRepository();
    }

    @Override
    public Optional<CurrencyModelDto> findByName(String name) {
        try {
            Optional<CurrencyModel> optional = repository.findByName(name);
            CurrencyModel currencyModel = optional.orElseThrow(HomeFinanceDaoException::new);
            validate(currencyModel);
            return Optional.of(new CurrencyModelDto(currencyModel));
        } catch (HomeFinanceDaoException e) {
            throw new HomeFinanceServiceException(e);
        } catch (HomeFinanceServiceException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<CurrencyModelDto> findAll() {
        Collection<CurrencyModel> models = repository.findAll();
        models.forEach(this::validate);

        return models.stream().map(CurrencyModelDto::new).collect(Collectors.toList());
    }

    @Override
    public boolean remove(Long id) {
        return repository.remove(id);
    }

    @Override
    public CurrencyModelDto save(CurrencyModelDto modelDto) {
        CurrencyModel model = new CurrencyModel().setId(modelDto.getId()).setCode(modelDto.getCode())
                                                .setName(modelDto.getName()).setSymbol(modelDto.getSymbol());

        currencyVerification(model);
        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            newModel = repository.save(model);
        }

        return new CurrencyModelDto(newModel);
    }

    @Override
    public CurrencyModelDto update(CurrencyModelDto modelDto) {
        CurrencyModel model = new CurrencyModel().setId(modelDto.getId()).setCode(modelDto.getCode())
                                                .setName(modelDto.getName()).setSymbol(modelDto.getSymbol());

        CurrencyModel newModel = new CurrencyModel();
        if (validate(model)) {
            newModel = repository.update(model);
        }

        return new CurrencyModelDto(newModel);
    }

    private boolean validate(CurrencyModel model) throws HomeFinanceServiceException {
        if (!validator.isValid(model)) {
            throw new HomeFinanceServiceException("Валюта " + model + " не валидирована");
        }

        return true;
    }

    private void currencyVerification(CurrencyModel model) throws HomeFinanceServiceException {
        String name = model.getName();
        repository.findByName(name).ifPresent(found -> {
            if (name.equals(found.getName())) {
                throw new HomeFinanceServiceException("Валюта уже существует");
            }
        });
    }
}
