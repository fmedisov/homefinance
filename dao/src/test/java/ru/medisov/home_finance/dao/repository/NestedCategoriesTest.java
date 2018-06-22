package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NestedCategoriesTest extends CommonRepositoryTest implements RepositoryTest {
    private TestModelGenerator generator = new TestModelGenerator();
    private CategoryRepository repository = new CategoryRepositoryImpl();

    @Test
    @DisplayName("Save Nested Models to database")
    void saveNestedCategoriesNonZeroIdReturned() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);

        assertEquals("Проезд", changed.getParent().getParent().getName());
        assertEquals("Авто", changed.getParent().getName());
        assertEquals("Бензин", changed.getName());
    }

    @Test
    @DisplayName("Attempt to save an one incorrect Model from nested models throws HomeFinanceDaoException")
    void saveIfOneModelIncorrectCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel modelWithLongName = modelWithParents.getParent().setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.saveWithParents(modelWithLongName));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing Models in the database")
    void findByNameIfExistsInDatabase() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);
        CategoryTransactionModel found = repository.findByName(modelWithParents.getName()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Search for all Models returns collection of models ")
    void findAllExistsThreeEntries() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel model = repository.saveWithParents(modelWithParents);
        Collection<CategoryTransactionModel> models = repository.findAll();

        assertEquals(3, models.size());
        assertTrue(models.contains(model));
        assertTrue(models.contains(model.getParent()));
        assertTrue(models.contains(model.getParent().getParent()));
    }

    @Test
    @DisplayName("Remove existing models returns true")
    void removeExistingEntriesReturnsTrue() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel model = repository.saveWithParents(modelWithParents);
        assertTrue(repository.remove(model.getId()));
        assertTrue(repository.remove(model.getParent().getId()));
        assertTrue(repository.remove(model.getParent().getParent().getId()));
    }

    @Test
    @DisplayName("Remove if links exists returns true")
    //requires a valid ddl database file for referential integrity
    void removeIfLinksExistsReturnsTrue() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel model = repository.saveWithParents(modelWithParents);

        assertTrue(repository.remove(model.getParent().getId()));
        assertNull(repository.findById(model.getId()).orElse(model).getParent());  // check that the parent is deleted
    }

    @Test
    @DisplayName("update parent of Model returns the correct model")
    //todo divide the method into two logical parts
    void updateParentModelCorrectModelReturned() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);
        CategoryTransactionModel remove = changed.getParent();
        changed.setParent(changed.getParent().getParent());

        CategoryTransactionModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new CategoryTransactionModel()));
        assertTrue(repository.remove(remove.getId()));  // for referential integrity
    }

    @Test
    @DisplayName("Search by id for an existing Models in the database")
    void findByIdIfExistsInDatabase() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);
        CategoryTransactionModel found = repository.findById(modelWithParents.getId()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent Models returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        CategoryTransactionModel modelWithParents = generator.generateCategoryWithParents();
        Optional<CategoryTransactionModel> found = repository.findById(modelWithParents.getId());
        assertEquals(found, Optional.empty());
    }
}