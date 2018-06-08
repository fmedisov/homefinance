package ru.medisov.home_finance.service;

import ru.medisov.home_finance.dao.model.TransactionModel;

import java.util.Collection;
import java.util.Optional;

public interface TransactionService {
    Optional<TransactionModel> findByName(String name);

    Collection<TransactionModel> findAll();

    boolean remove(Long id);

    TransactionModel save(TransactionModel model);

    TransactionModel update(TransactionModel model);
}
