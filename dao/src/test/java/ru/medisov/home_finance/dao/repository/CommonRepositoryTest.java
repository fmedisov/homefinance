package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.dao.DaoConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonRepositoryTest {

    @BeforeAll
    static void initConfig()  {
        DaoConfig.initConfig();
    }

    @BeforeEach
    public void truncateAllTables() {
        String sqlQuery =
                "SET FOREIGN_KEY_CHECKS=0; " +
                        "TRUNCATE TABLE `currency_tbl`; " +
                        "TRUNCATE TABLE `account_tbl`; " +
                        "TRUNCATE TABLE `category_tbl`; " +
                        "TRUNCATE TABLE `transaction_tbl`; " +
                        "TRUNCATE TABLE `tag_relation_tbl`; " +
                        "TRUNCATE TABLE `tag_tbl`; " +
                        "SET FOREIGN_KEY_CHECKS=1;";
        try (Connection connection = new DbConnectionBuilder().getConnection()) {
            connection.prepareStatement(sqlQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T extends TagModel> void saveCorrectModelNonNullIdReturned(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        T model = TestModel.generateModel(aModelClass);

        T actual = repository.save(model);

        assertTrue(actual.getId() != null);
    }

    public <T extends TagModel> void findByNameIfExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        T expected = repository.save(TestModel.generateModel(aModelClass));

        //act
        T actual = null;
        try {
            actual = repository.findByName(expected.getName()).orElse(aModelClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> void findByNameIfNotExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        Optional<T> expected = Optional.empty();
        T model = TestModel.generateModel(aModelClass);

        //act
        Optional<T> actual = repository.findByName(model.getName());

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> void findAllExistsOneEntry(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        int expectedSize = 1;
        T model = TestModel.generateModel(aModelClass);
        T expectedModel = repository.save(model);

        //act
        Collection<T> actual = repository.findAll();

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
    }

    public <T extends TagModel> void findAllEmptyTable(ExtendedRepository<T, Long> repository) {
        Collection<T> models = repository.findAll();

        assertTrue(models.size() == 0);
    }

    public <T extends TagModel> void removeExistingEntryReturnsTrue(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        T model = repository.save(TestModel.generateModel(aModelClass));

        assertTrue(repository.remove(model.getId()));
        assertTrue(repository.findAll().size() == 0);
    }

    public <T extends TagModel> void removeIfNotExistsReturnsFalse(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        T model = TestModel.generateModel(aModelClass);
        assertFalse(repository.remove(model.getId()));
    }

    public <T extends TagModel> void findByIdIfExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        T expected = repository.save(TestModel.generateModel(aModelClass));

        //act
        T actual = null;
        try {
            actual = repository.findById(expected.getId()).orElse(aModelClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> void findByIdIfNotExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        Optional<T> expected = Optional.empty();
        T model = TestModel.generateModel(aModelClass);

        //act
        Optional<T> actual = repository.findById(model.getId());

        //assert
        assertEquals(expected, actual);
    }
}
