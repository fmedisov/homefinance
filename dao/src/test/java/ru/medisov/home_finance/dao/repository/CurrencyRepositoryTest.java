package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.dao.DaoConfig;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CurrencyModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyRepositoryTest {
    private CurrencyModel currencyModel = getCurrencyModel();
    private CurrencyRepository repository = new CurrencyRepository();

    @BeforeAll
    static void initConfig() {
        DaoConfig.initConfig();
        System.out.println("init config");
    }

    @BeforeEach
    void initDatabase() {
        try (Connection connection = new DbConnectionBuilder().getConnection()) {
            connection.prepareStatement(initDDL()).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Save correct CurrencyModel to database")
    public void saveCorrectModelNonZeroIdReturned() {
        CurrencyModel changed = repository.save(currencyModel);
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save an incorrect CurrencyModel to database throws HomeFinanceDaoException")
    public void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        CurrencyModel modelWithLongCode = getCurrencyModel().setCode("Too long code");
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongCode));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing currency in the database")
    public void findByNameIfExistsInDatabase() {
        CurrencyModel changed = repository.save(currencyModel);
        CurrencyModel found = repository.findByName(currencyModel.getName()).orElse(new CurrencyModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent currency returns Optional.empty()")
    public void findByNameIfNotExistsInDatabase() {
        Optional<CurrencyModel> found = repository.findByName(currencyModel.getName());
        assertEquals(found, Optional.empty());
    }

    @Test
    @DisplayName("Search for all currency models returns collection of models ")
    public void findAllExistsOneEntry() {
        CurrencyModel model = repository.save(currencyModel);
        Collection<CurrencyModel> models = repository.findAll();
        assertEquals(1, models.size());
        assertTrue(models.contains(model));
    }

    @Test
    @DisplayName("Attempt to search currency models in empty table returns empty collection")
    public void findAllEmptyTable() {
        Collection<CurrencyModel> models = repository.findAll();
        assertEquals(0, models.size());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    public void removeExistingEntryReturnsTrue() {
        CurrencyModel model = repository.save(currencyModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    public void removeIfNotExistsReturnsFalse() {
        assertFalse(repository.remove(currencyModel.getId()));
    }

    @Test
    @DisplayName("update correct CurrencyModel returns the same model")
    public void updateCorrectModelSameModelReturned() {
        CurrencyModel changed = repository.save(currencyModel).setName("Эскудо").setCode("CVE");
        CurrencyModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new CurrencyModel()));
    }

    @Test
    @DisplayName("Attempt to update an incorrect CurrencyModel throws HomeFinanceDaoException")
    public void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        CurrencyModel modelWithLongCode = repository.save(currencyModel).setCode("Too long code");
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongCode));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing currency in the database")
    public void findByIdIfExistsInDatabase() {
        CurrencyModel changed = repository.save(currencyModel);
        CurrencyModel found = repository.findById(currencyModel.getId()).orElse(new CurrencyModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent currency returns Optional.empty()")
    public void findByIdIfNotExistsInDatabase() {
        Optional<CurrencyModel> found = repository.findById(currencyModel.getId());
        assertEquals(found, Optional.empty());
    }

    private CurrencyModel getCurrencyModel() {
        return new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
    }

    private String initDDL() {
        return "DROP TABLE IF EXISTS `currency_tbl`; CREATE TABLE IF NOT EXISTS `currency_tbl`" +
                " ( `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `name` VARCHAR(100) NULL,`code` VARCHAR(5) NULL,`symbol` VARCHAR(5) NULL);";
    }
}