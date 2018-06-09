package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrencyServiceImpl implements CurrencyService {
    @Override
    public Optional<CurrencyModelDto> findByName(String name) {
        Optional<CurrencyModel> optional = new CurrencyRepository().findByName(name);
        return Optional.of(new CurrencyModelDto(optional.get()));
    }

    @Override
    public Collection<CurrencyModelDto> findAll() {
        Collection<CurrencyModel> collection = new CurrencyRepository().findAll();
        return collection.stream().map(CurrencyModelDto::new).collect(Collectors.toList());
    }

    @Override
    public boolean remove(Long id) {
        return new CurrencyRepository().remove(id);
    }

    @Override
    public CurrencyModelDto save(CurrencyModelDto modelDto) {
        CurrencyModel model = new CurrencyModel().setId(modelDto.getId()).setCode(modelDto.getCode())
                .setName(modelDto.getName()).setSymbol(modelDto.getSymbol());
        return new CurrencyModelDto(new CurrencyRepository().save(model));
    }

    @Override
    public CurrencyModelDto update(CurrencyModelDto modelDto) {
        CurrencyModel model = new CurrencyModel().setId(modelDto.getId()).setCode(modelDto.getCode())
                .setName(modelDto.getName()).setSymbol(modelDto.getSymbol());
        return new CurrencyModelDto(new CurrencyRepository().update(model));
    }
}
