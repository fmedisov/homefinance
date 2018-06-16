package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.dao.DaoConfig;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CategoryTransactionModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NestedCategoriesTest {
    private CategoryTransactionModel modelWithParents = getModelWithParents();
    private CategoryRepository repository = new CategoryRepository();

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
    @DisplayName("Save Nested Models to database")
    void saveNestedCategoriesNonZeroIdReturned() {
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);

        assertEquals(1, changed.getParent().getParent().getId());
        assertEquals(2, changed.getParent().getId());
        assertEquals(3, changed.getId());
    }

    @Test
    @DisplayName("Attempt to save an one incorrect Model from nested models throws HomeFinanceDaoException")
    void saveIfOneModelIncorrectCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithLongName = getModelWithParents().getParent().setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.saveWithParents(modelWithLongName));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing Models in the database")
    void findByNameIfExistsInDatabase() {
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);
        CategoryTransactionModel found = repository.findByName(modelWithParents.getName()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Search for all Models returns collection of models ")
    void findAllExistsThreeEntries() {
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
        CategoryTransactionModel model = repository.saveWithParents(modelWithParents);
        assertTrue(repository.remove(model.getId()));
        assertTrue(repository.remove(model.getParent().getId()));
        assertTrue(repository.remove(model.getParent().getParent().getId()));
    }

    @Test
    @DisplayName("Remove existing models returns true")
    //requires a valid ddl database file for referential integrity
    void removeIfLinksExistsCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel model = repository.saveWithParents(modelWithParents);

        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.remove(model.getParent().getId()));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("update parent of Model returns the correct model")
    //todo divide the method into two logical parts
    void updateParentModelCorrectModelReturned() {
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
        CategoryTransactionModel changed = repository.saveWithParents(modelWithParents);
        CategoryTransactionModel found = repository.findById(modelWithParents.getId()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent Models returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        Optional<CategoryTransactionModel> found = repository.findById(modelWithParents.getId());
        assertEquals(found, Optional.empty());
    }

    private CategoryTransactionModel getModelWithParents() {
        CategoryTransactionModel mainParent = new CategoryTransactionModel().setName("Проезд");
        CategoryTransactionModel parent = new CategoryTransactionModel().setName("Авто");
        CategoryTransactionModel category = new CategoryTransactionModel().setName("Бензин");
        parent.setParent(mainParent);
        category.setParent(parent);
        return category;
    }

    private String getLongName() {
        return "Long long long long long long long long long long long long long long long long long long category name";
    }

    private String initDDL() {
        return "DROP TABLE IF EXISTS `category_tbl`; CREATE TABLE IF NOT EXISTS `category_tbl` ( " +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "`name` VARCHAR(45) NULL, " +
                    "`parent` INT NULL, " +
                "CONSTRAINT `parent_category_fk` " +
                "FOREIGN KEY (`parent`) " +
                "REFERENCES `category_tbl` (`id`));";
    }
}