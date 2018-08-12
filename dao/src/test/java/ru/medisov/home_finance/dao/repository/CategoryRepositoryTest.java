package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.dao.config.DaoConfiguration;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfiguration.class})
class CategoryRepositoryTest extends CommonRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonNullIdReturned() {
        super.saveCorrectModelNonNullIdReturned(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel categoryModel = TestModel.generateCategoryModel();
        CategoryTransactionModel modelWithLongName = categoryModel.setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongName));

    }

    @Test
    @DisplayName("Search by name for an existing Model in the database")
    void findByNameIfExistsInDatabase() {
        super.findByNameIfExistsInDatabase(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent Model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        super.findByNameIfNotExistsInDatabase(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("Search for all Models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repository);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        //arrange
        CategoryTransactionModel categoryModel = TestModel.generateCategoryModel();
        CategoryTransactionModel expected = repository.save(categoryModel).setName("Дорога");

        //act
        CategoryTransactionModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel categoryModel = TestModel.generateCategoryModel();
        CategoryTransactionModel modelWithLongName = repository.save(categoryModel).setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongName));
    }

    @Test
    @DisplayName("Search by id for an existing Model in the database")
    void findByIdIfExistsInDatabase() {
        super.findByIdIfExistsInDatabase(repository, CategoryTransactionModel.class);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent Model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        super.findByIdIfNotExistsInDatabase(repository, CategoryTransactionModel.class);
    }
}