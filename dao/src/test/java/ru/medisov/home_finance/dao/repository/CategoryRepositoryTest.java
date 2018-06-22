package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.*;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest extends CommonRepositoryTest implements RepositoryTest {
    private TestModelGenerator generator = new TestModelGenerator();
    private CategoryRepository repository = new CategoryRepositoryImpl();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonZeroIdReturned() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel changed = repository.save(categoryModel);
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel modelWithLongName = categoryModel.setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongName));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing Model in the database")
    void findByNameIfExistsInDatabase() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel changed = repository.save(categoryModel);
        CategoryTransactionModel found = repository.findByName(categoryModel.getName()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent Model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        Optional<CategoryTransactionModel> found = repository.findByName(categoryModel.getName());
        assertEquals(found, Optional.empty());
    }

    @Test
    @DisplayName("Search for all Models returns collection of models ")
    void findAllExistsOneEntry() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel model = repository.save(categoryModel);
        Collection<CategoryTransactionModel> models = repository.findAll();
        assertEquals(1, models.size());
        assertTrue(models.contains(model));
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<CategoryTransactionModel> models = repository.findAll();
        assertEquals(0, models.size());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel model = repository.save(categoryModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        assertFalse(repository.remove(categoryModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel changed = repository.save(categoryModel).setName("Дорога");
        CategoryTransactionModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new CategoryTransactionModel()));
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel modelWithLongName = repository.save(categoryModel).setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing Model in the database")
    void findByIdIfExistsInDatabase() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        CategoryTransactionModel changed = repository.save(categoryModel);
        CategoryTransactionModel found = repository.findById(categoryModel.getId()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent Model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        CategoryTransactionModel categoryModel = generator.generateCategoryModel();
        Optional<CategoryTransactionModel> found = repository.findById(categoryModel.getId());
        assertEquals(found, Optional.empty());
    }
}