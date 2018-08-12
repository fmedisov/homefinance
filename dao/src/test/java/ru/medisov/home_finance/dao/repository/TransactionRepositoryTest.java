package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.utils.MoneyUtils;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.config.DaoConfiguration;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfiguration.class})
class TransactionRepositoryTest extends CommonRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonNullIdReturned() {
        super.saveCorrectModelNonNullIdReturned(repository, TransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to save a Model without transaction type throws HomeFinanceDaoException")
    void saveWithoutTransactionTypeCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel withoutTransactionType = transactionModel.setTransactionType(null);

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(withoutTransactionType));
    }

    @Test
    @DisplayName("Attempt to save a Model with too long name throws HomeFinanceDaoException")
    void saveWithLongNameCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel withLongName = transactionModel.setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(withLongName));
    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        super.findByNameIfExistsInDatabase(repository, TransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        super.findByNameIfNotExistsInDatabase(repository, TransactionModel.class);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repository, TransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repository);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repository, TransactionModel.class);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repository, TransactionModel.class);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        //arrange
        TransactionModel transactionModel = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel expected = repository
                .save(transactionModel)
                .setName("Бензин 98")
                .setAmount(MoneyUtils.inBigDecimal(12345));

        //act
        TransactionModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to update model without transaction type throws HomeFinanceDaoException")
    void updateWithoutTransactionTypeCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel withoutTransactionType = repository.save(transactionModel).setTransactionType(null);

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(withoutTransactionType));
    }

    @Test
    @DisplayName("Attempt to update model with too long name throws HomeFinanceDaoException")
    void updateWithTooLongNameCausesException() throws HomeFinanceDaoException {
        TransactionModel transactionModel = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel withLongName = repository.save(transactionModel).setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(withLongName));
    }

    @Test
    @DisplayName("Attempt to search models by period in empty table returns empty collection")
    void findByPeriodEmptyTable() {
        Collection<TransactionModel> models = repository.findByPeriod(LocalDateTime.MIN, LocalDateTime.MAX);

        assertTrue(models.size() == 0);
    }

    @Test
    @DisplayName("Search for models by period returns collection of models ")
    void findByPeriodExistsOneEntry() {
        //arrange
        int expectedSize = 1;
        TransactionModel model = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel expectedModel = repository.save(model);

        //act
        Collection<TransactionModel> actual = repository.findByPeriod(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
    }

    @Test
    @DisplayName("Search for models by period if the start date is longer than the end date, returns empty collection")
    void findByPeriodIncorrectPeriodEmptyCollection() {
        //arrange
        repository.save(super.generateModelWithSavedFields(TransactionModel.class));

        //act
        Collection<TransactionModel> actual = repository.findByPeriod(LocalDateTime.now().plusYears(1000), LocalDateTime.now().minusYears(1000));

        //assert
        assertTrue(actual.size() == 0);
    }

    @Test
    @DisplayName("Search for models by period if the models absence in the specified period, returns empty collection")
    void findByPeriodAbsenceInPeriodReturnsEmptyCollection() {
        //arrange
        repository.save(super.generateModelWithSavedFields(TransactionModel.class).setDateTime(LocalDateTime.now().minusYears(1)));

        //act
        Collection<TransactionModel> actual = repository.findByPeriod(LocalDateTime.MIN, LocalDateTime.now().minusYears(3));

        //assert
        assertTrue(actual.size() == 0);
    }

    @Test
    @DisplayName("Search for models by period if dates are null, returns empty collection")
    void findByPeriodNullDateReturnsEmptyCollection() {
        //arrange
        repository.save(super.generateModelWithSavedFields(TransactionModel.class));

        //act
        Collection<TransactionModel> actual = repository.findByPeriod(null, null);

        //assert
        assertTrue(actual.size() == 0);
    }

    @Test
    @DisplayName("Attempt to search models by category in empty table returns empty collection")
    void findByCategoryEmptyTable() {
        Collection<TransactionModel> models = repository.findByCategory(1L);

        assertTrue(models.size() == 0);
    }

    @Test
    @DisplayName("Search for models by category returns collection of models ")
    void findByCategoryExistsOneEntry() {
        //arrange
        int expectedSize = 1;
        TransactionModel model = super.generateModelWithSavedFields(TransactionModel.class);
        TransactionModel expectedModel = repository.save(model);

        //act
        Collection<TransactionModel> actual = repository.findByCategory(expectedModel.getCategory().getId());

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
    }

    @Test
    @DisplayName("Search models by null category returns collection of models")
    void findByCategoryNullCategoryExistsOneEntry() {
        //arrange
        int expectedSize = 1;
        TransactionModel model = super.generateModelWithSavedFields(TransactionModel.class).setCategory(null);
        TransactionModel expectedModel = repository.save(model);

        //act
        Collection<TransactionModel> actual = repository.findByCategory(null);

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
    }

    @Test
    @DisplayName("Attempt to search models by null category returns empty collection")
    void findByCategoryNullCategoryReturnsEmptyCollection() {
        //arrange
        repository.save(super.generateModelWithSavedFields(TransactionModel.class));

        //act
        Collection<TransactionModel> actual = repository.findByCategory(null);

        //assert
        assertTrue(actual.size() == 0);
    }

    @Test
    @DisplayName("Attempt to search income transactions in empty table returns empty collection")
    void incomeByPeriodEmptyTable() {
        Collection<TransactionModel> models = repository.incomeByPeriod(LocalDateTime.MIN, LocalDateTime.MAX);

        assertTrue(models.size() == 0);
    }

    @Test
    @DisplayName("Search for income transactions by period returns collection of models ")
    void incomeByPeriodExistsOneEntry() {
        //arrange
        int expectedSize = 1;
        TransactionModel model = super.generateModelWithSavedFields(TransactionModel.class).setTransactionType(TransactionType.INCOME);
        TransactionModel expectedModel = repository.save(model);

        //act
        Collection<TransactionModel> actual = repository.findByPeriod(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
    }

    @Test
    @DisplayName("Attempt to search expense transactions in empty table returns empty collection")
    void expenseByPeriodEmptyTable() {
        Collection<TransactionModel> models = repository.expenseByPeriod(LocalDateTime.MIN, LocalDateTime.MAX);

        assertTrue(models.size() == 0);
    }
}