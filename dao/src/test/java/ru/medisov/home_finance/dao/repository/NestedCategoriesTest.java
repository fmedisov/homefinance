package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NestedCategoriesTest extends CommonRepositoryTest {
    private CategoryRepository repository = new CategoryRepositoryImpl();

    @Test
    @DisplayName("Save Nested Models to database")
    void saveNestedCategoriesNonNullIdReturned() {
        //arrange
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();

        //act
        CategoryTransactionModel actual = repository.saveWithParents(modelWithParents);

        //assert
        assertEquals("Проезд", actual.getParent().getParent().getName());
        assertEquals("Авто", actual.getParent().getName());
        assertEquals("Бензин", actual.getName());
    }

    @Test
    @DisplayName("Attempt to save an one incorrect Model from nested models throws HomeFinanceDaoException")
    void saveIfOneModelIncorrectCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();
        CategoryTransactionModel modelWithLongName = modelWithParents.getParent().setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.saveWithParents(modelWithLongName));
    }

    @Test
    @DisplayName("Search by name for an existing Models in the database")
    void findByNameIfExistsInDatabase() {
        //arrange
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();
        CategoryTransactionModel expected = repository.saveWithParents(modelWithParents);

        //act
        CategoryTransactionModel actual = repository.findByName(modelWithParents.getName()).orElse(new CategoryTransactionModel());

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Search for all Models returns collection of models ")
    void findAllExistsThreeEntries() {
        //arrange
        int expectedSize = 3;
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();
        CategoryTransactionModel expectedModel = repository.saveWithParents(modelWithParents);

        //act
        Collection<CategoryTransactionModel> actual = repository.findAll();

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
        assertTrue(actual.contains(expectedModel.getParent()));
        assertTrue(actual.contains(expectedModel.getParent().getParent()));
    }

    @Test
    @DisplayName("Remove existing models returns true")
    void removeExistingEntriesReturnsTrue() {
        //arrange
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();

        //act
        CategoryTransactionModel actual = repository.saveWithParents(modelWithParents);

        //assert
        assertTrue(repository.remove(actual.getId()));
        assertTrue(repository.remove(actual.getParent().getId()));
        assertTrue(repository.remove(actual.getParent().getParent().getId()));
    }

    @Test
    @DisplayName("Remove if links exists returns true")
    //requires a valid ddl database file for referential integrity
    void removeIfLinksExistsReturnsTrue() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();
        CategoryTransactionModel model = repository.saveWithParents(modelWithParents);

        assertTrue(repository.remove(model.getParent().getId()));
        assertNull(repository.findById(model.getId()).orElse(model).getParent());  // check that the parent is deleted
    }

    @Test
    @DisplayName("update parent of Model returns the correct model")
    void updateParentModelCorrectModelReturned() {
        //arrange
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();
        CategoryTransactionModel expected = repository.saveWithParents(modelWithParents);
        CategoryTransactionModel remove = expected.getParent();
        expected.setParent(expected.getParent().getParent());

        //act
        CategoryTransactionModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
        assertTrue(repository.remove(remove.getId()));  // for referential integrity
    }

    @Test
    @DisplayName("Search by id for an existing Models in the database")
    void findByIdIfExistsInDatabase() {
        //arrange
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();
        CategoryTransactionModel expected = repository.saveWithParents(modelWithParents);

        //act
        CategoryTransactionModel actual = repository.findById(modelWithParents.getId()).orElse(new CategoryTransactionModel());

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent Models returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        //arrange
        Optional<CategoryTransactionModel> expected = Optional.empty();
        CategoryTransactionModel modelWithParents = TestModel.generateCategoryWithParents();

        //act
        Optional<CategoryTransactionModel> actual = repository.findById(modelWithParents.getId());

        //assert
        assertEquals(expected, actual);
    }
}