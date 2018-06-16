package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.*;
import ru.medisov.home_finance.dao.DaoConfig;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.dao.model.CategoryTransactionModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {
    private CategoryTransactionModel categoryModel = getCategoryModel();
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
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonZeroIdReturned() {
        CategoryTransactionModel changed = repository.save(categoryModel);
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithLongName = getCategoryModel().setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongName));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing Model in the database")
    void findByNameIfExistsInDatabase() {
        CategoryTransactionModel changed = repository.save(categoryModel);
        CategoryTransactionModel found = repository.findByName(categoryModel.getName()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent Model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        Optional<CategoryTransactionModel> found = repository.findByName(categoryModel.getName());
        assertEquals(found, Optional.empty());
    }

    @Test
    @DisplayName("Search for all Models returns collection of models ")
    void findAllExistsOneEntry() {
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
        CategoryTransactionModel model = repository.save(categoryModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        assertFalse(repository.remove(categoryModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        CategoryTransactionModel changed = repository.save(categoryModel).setName("Дорога");
        CategoryTransactionModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new CategoryTransactionModel()));
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        CategoryTransactionModel modelWithLongName = repository.save(categoryModel).setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing Model in the database")
    void findByIdIfExistsInDatabase() {
        CategoryTransactionModel changed = repository.save(categoryModel);
        CategoryTransactionModel found = repository.findById(categoryModel.getId()).orElse(new CategoryTransactionModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent Model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        Optional<CategoryTransactionModel> found = repository.findById(categoryModel.getId());
        assertEquals(found, Optional.empty());
    }

    private CategoryTransactionModel getCategoryModel() {
        return new CategoryTransactionModel().setName("Проезд").setParent(null);
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