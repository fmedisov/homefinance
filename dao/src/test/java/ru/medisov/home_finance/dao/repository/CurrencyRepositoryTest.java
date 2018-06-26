package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.*;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.common.generator.TestModelGenerator;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyRepositoryTest extends CommonRepositoryTest implements RepositoryTest {
    private TestModelGenerator generator = new TestModelGenerator();
    private CurrencyRepository repository = new CurrencyRepositoryImpl();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonNullIdReturned() {
        super.saveCorrectModelNonNullIdReturned(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel modelWithLongCode = currencyModel.setCode("Too long code");

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongCode));

    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        super.findByNameIfExistsInDatabase(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        super.findByNameIfNotExistsInDatabase(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repository);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        //arrange
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel expected = repository.save(currencyModel).setName("Эскудо").setCode("CVE");

        //act
        CurrencyModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel modelWithLongCode = repository.save(currencyModel).setCode("Too long code");

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongCode));
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        super.findByIdIfExistsInDatabase(repository, generator, CurrencyModel.class);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        super.findByIdIfNotExistsInDatabase(repository, generator, CurrencyModel.class);
    }
}