package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest extends AbstractRepositoryTest {
    private TransactionModel transactionModel = getTransactionModel();
    private TransactionRepository repository = new TransactionRepository();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonZeroIdReturned() {
        TransactionModel changed = repository.save(transactionModel);
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save a Model without transaction type throws HomeFinanceDaoException")
    void saveWithoutTransactionTypeCausesException() throws HomeFinanceDaoException {
        TransactionModel withoutTransactionType = getTransactionModel().setTransactionType(null);
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(withoutTransactionType));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Attempt to save a Model with too long name throws HomeFinanceDaoException")
    void saveWithLongNameCausesException() throws HomeFinanceDaoException {
        TransactionModel withLongName = getTransactionModel().setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(withLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    //todo remove duplicate code and create abstractRepositoryTest class
    void findByNameIfExistsInDatabase() {
        TransactionModel changed = repository.save(transactionModel);
        TransactionModel found = repository.findByName(changed.getName()).orElse(new TransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        Optional<TransactionModel> found = repository.findByName(transactionModel.getName());
        assertEquals(found, Optional.empty());
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        TransactionModel model = repository.save(transactionModel);
        Collection<TransactionModel> models = repository.findAll();
        assertEquals(1, models.size());
        assertTrue(models.contains(model));
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<TransactionModel> models = repository.findAll();
        assertEquals(0, models.size());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        TransactionModel model = repository.save(transactionModel);
        assertTrue(repository.remove(model.getId()));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        assertFalse(repository.remove(transactionModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        TransactionModel changed = repository.save(transactionModel)
                .setName("Бензин 98").setAmount(getBaseAmount().add(BigDecimal.valueOf(12345)));

        TransactionModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new TransactionModel()));
    }

    @Test
    @DisplayName("Attempt to update model without transaction type throws HomeFinanceDaoException")
    void updateWithoutTransactionTypeCausesException() throws HomeFinanceDaoException {
        TransactionModel withoutTransactionType = repository.save(transactionModel).setTransactionType(null);
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(withoutTransactionType));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Attempt to update model with too long name throws HomeFinanceDaoException")
    void updateWithTooLongNameCausesException() throws HomeFinanceDaoException {
        TransactionModel withLongName = repository.save(transactionModel).setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(withLongName));
        assertNotNull(thrown.getMessage());
    }

    private TransactionModel getTransactionModel() {
        AccountModel accountModel = getAccountModel();
        CategoryTransactionModel category = getCategoryModel();
        List<TagModel> tags = getTags();

        return new TransactionModel().setTransactionType(TransactionType.EXPENSE)
                .setAccount(accountModel).setCategory(category).setDateTime(LocalDateTime.now())
                .setAmount(getBaseAmount().add(BigDecimal.valueOf(3000))).setName("Бензин 95")
                .setTags(tags);
    }
}