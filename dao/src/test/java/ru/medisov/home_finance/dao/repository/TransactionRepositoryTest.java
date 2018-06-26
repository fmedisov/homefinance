package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest extends CommonRepositoryTest implements RepositoryTest {
    private TestModelGenerator generator = new TestModelGenerator();
    private TransactionRepository repository = new TransactionRepositoryImpl();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonNullIdReturned() {
        super.saveCorrectModelNonNullIdReturned(repository, generator, TransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to save a Model without transaction type throws HomeFinanceDaoException")
    void saveWithoutTransactionTypeCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = generator.generateTransactionModel();
        TransactionModel withoutTransactionType = transactionModel.setTransactionType(null);

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(withoutTransactionType));
    }

    @Test
    @DisplayName("Attempt to save a Model with too long name throws HomeFinanceDaoException")
    void saveWithLongNameCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = generator.generateTransactionModel();
        TransactionModel withLongName = transactionModel.setName(generator.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(withLongName));
    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        super.findByNameIfExistsInDatabase(repository, generator, TransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        super.findByNameIfNotExistsInDatabase(repository, generator, TransactionModel.class);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repository, generator, TransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repository);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repository, generator, TransactionModel.class);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repository, generator, TransactionModel.class);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        //arrange
        TransactionModel transactionModel = generator.generateTransactionModel();
        TransactionModel expected = repository.save(transactionModel)
                .setName("Бензин 98").setAmount(generator.getBaseAmount().add(BigDecimal.valueOf(12345)));

        //act
        TransactionModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to update model without transaction type throws HomeFinanceDaoException")
    void updateWithoutTransactionTypeCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = generator.generateTransactionModel();
        TransactionModel withoutTransactionType = repository.save(transactionModel).setTransactionType(null);

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(withoutTransactionType));
    }

    @Test
    @DisplayName("Attempt to update model with too long name throws HomeFinanceDaoException")
    void updateWithTooLongNameCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = generator.generateTransactionModel();
        TransactionModel withLongName = repository.save(transactionModel).setName(generator.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(withLongName));
    }
}