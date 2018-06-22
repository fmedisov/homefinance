package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.*;
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
    void saveCorrectModelNonZeroIdReturned() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel changed = repository.save(currencyModel);
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel modelWithLongCode = currencyModel.setCode("Too long code");
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongCode));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel changed = repository.save(currencyModel);
        CurrencyModel found = repository.findByName(currencyModel.getName()).orElse(new CurrencyModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        Optional<CurrencyModel> found = repository.findByName(currencyModel.getName());
        assertEquals(found, Optional.empty());
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel model = repository.save(currencyModel);
        Collection<CurrencyModel> models = repository.findAll();
        assertEquals(1, models.size());
        assertTrue(models.contains(model));
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<CurrencyModel> models = repository.findAll();
        assertEquals(0, models.size());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel model = repository.save(currencyModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        assertFalse(repository.remove(currencyModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel changed = repository.save(currencyModel).setName("Эскудо").setCode("CVE");
        CurrencyModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new CurrencyModel()));
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel modelWithLongCode = repository.save(currencyModel).setCode("Too long code");
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongCode));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        CurrencyModel changed = repository.save(currencyModel);
        CurrencyModel found = repository.findById(currencyModel.getId()).orElse(new CurrencyModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        CurrencyModel currencyModel = generator.generateCurrencyModel();
        Optional<CurrencyModel> found = repository.findById(currencyModel.getId());
        assertEquals(found, Optional.empty());
    }
}