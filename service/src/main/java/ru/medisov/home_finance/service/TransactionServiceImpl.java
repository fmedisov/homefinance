package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.TransactionModel;
import ru.medisov.home_finance.dao.repository.TransactionRepository;

import java.util.Collection;
import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public Optional<TransactionModel> findByName(String name) {
        return new TransactionRepository().findByName(name);
    }

    @Override
    public Collection<TransactionModel> findAll() {
        return new TransactionRepository().findAll();
    }

    @Override
    public boolean remove(Long id) {
        return new TransactionRepository().remove(id);
    }

    @Override
    public TransactionModel save(TransactionModel model) {
        return new TransactionRepository().save(model);
    }

    @Override
    public TransactionModel update(TransactionModel model) {
        return new TransactionRepository().update(model);
    }
}
