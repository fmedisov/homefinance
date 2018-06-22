package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest extends AbstractRepositoryTest {
    private TestModelGenerator generator = new TestModelGenerator();
    private AccountRepository repository = new AccountRepository();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonZeroIdReturned() {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel changed = repository.save(accountModel);
        //todo id - Long object, check for null
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save a Model without account type throws HomeFinanceDaoException")
    void saveWithoutAccTypeCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel withoutAccType = accountModel.setAccountType(null);
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(withoutAccType));
        //todo check message or remove it
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Attempt to save a Model with too long name throws HomeFinanceDaoException")
    void saveWithLongNameCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel withLongName = accountModel.setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(withLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    //todo remove duplicate code and create abstractRepositoryTest class
    void findByNameIfExistsInDatabase() {
        //todo rename models (expected and actual)
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel changed = repository.save(accountModel);
        AccountModel found = repository.findByName(accountModel.getName()).orElse(new AccountModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        AccountModel accountModel = generator.generateAccountModel();
        Optional<AccountModel> found = repository.findByName(accountModel.getName());
        assertEquals(found, Optional.empty());
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel model = repository.save(accountModel);
        Collection<AccountModel> models = repository.findAll();
        assertEquals(1, models.size());
        assertTrue(models.contains(model));
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<AccountModel> models = repository.findAll();
        //todo add assertFalse
        assertEquals(0, models.size());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel model = repository.save(accountModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        AccountModel accountModel = generator.generateAccountModel();
        assertFalse(repository.remove(accountModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        AccountModel accountModel = generator.generateAccountModel();
        CurrencyModel currencyModel = new CurrencyRepository().save(new CurrencyModel()
                                            .setName("US dollar").setCode("USD").setSymbol("$"));
        AccountModel changed = repository.save(accountModel)
                .setAccountType(AccountType.CREDIT_CARD).setName("Citibank Card")
                .setCurrencyModel(currencyModel).setAmount(generator.getBaseAmount().add(BigDecimal.valueOf(12345)));

        AccountModel updated = repository.update(changed);
        //todo use updated model
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new AccountModel()));
    }

    @Test
    @DisplayName("Attempt to update model without account type throws HomeFinanceDaoException")
    void updateWithoutAccTypeCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel withoutAccType = repository.save(accountModel).setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(withoutAccType));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Attempt to update model with too long name throws HomeFinanceDaoException")
    void updateWithTooLongNameCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel withLongName = repository.save(accountModel).setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(withLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        AccountModel accountModel = generator.generateAccountModel();
        AccountModel changed = repository.save(accountModel);
        AccountModel found = repository.findById(accountModel.getId()).orElse(new AccountModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        AccountModel accountModel = generator.generateAccountModel();
        Optional<AccountModel> found = repository.findById(accountModel.getId());
        assertEquals(found, Optional.empty());
    }
}