package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.TagModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest extends CommonRepositoryTest {
    private TagRepository repository = new TagRepositoryImpl();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonNullIdReturned() {
        super.saveCorrectModelNonNullIdReturned(repository, TagModel.class);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        TagModel tagModel = TestModel.generateTagModel();
        TagModel modelWithLongName = tagModel.setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongName));
    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        super.findByNameIfExistsInDatabase(repository, TagModel.class);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        super.findByNameIfNotExistsInDatabase(repository, TagModel.class);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repository, TagModel.class);
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repository);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repository, TagModel.class);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repository, TagModel.class);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        //arrange
        TagModel tagModel = TestModel.generateTagModel();
        TagModel expected = repository.save(tagModel).setName("@проезд").setCount(2);

        //act
        TagModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        TagModel tagModel = TestModel.generateTagModel();
        TagModel modelWithLongName = repository.save(tagModel).setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongName));
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        super.findByIdIfExistsInDatabase(repository, TagModel.class);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        super.findByIdIfNotExistsInDatabase(repository, TagModel.class);
    }

    @Test
    @DisplayName("Save correct Model list returns the models with id")
    void saveTagListCorrectModelsReturnsModelsWithId() {
        //arrange
        List<TagModel> tags = TestModel.generateTags();

        //act
        List<TagModel> actual = repository.saveTagList(tags);

        //assert
        assertTrue(actual.size() > 0);
        assertTrue(actual.stream().allMatch(tag -> tag.getId() != null));
    }

    @Test
    @DisplayName("Attempt to save empty tag list returns empty list")
    void saveTagListEmptyList() {
        //arrange
        List<TagModel> expected = new ArrayList<>();

        //act
        List<TagModel> actual = repository.saveTagList(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Replace correct Model list returns the same models and the find method finds the replaced models")
    void updateTagListCorrectModelsReturnsModelsWithId() {
        //arrange
        List<TagModel> tags = TestModel.generateTags();
        List<TagModel> saved = repository.saveTagList(tags);
        saved.get(0).setName("буратино");
        saved.get(1).setName("кока-кола");
        saved.get(2).setName("тархун");

        //act
        List<TagModel> actual = repository.updateTagList(saved);
        List<TagModel> expected = new ArrayList<>(repository.findAll());

        //assert
        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    @Disabled
    @Test
    @DisplayName("Replace Model list if the list contains unsaved models")
    // todo refine the method updateTagList
    void updateTagListIfContainsUnsavedModels() {
        //arrange
        List<TagModel> tags = TestModel.generateTags();
        List<TagModel> saved = repository.saveTagList(tags);
        saved.get(0).setName("буратино");
        saved.get(1).setName("кока-кола");
        saved.get(2).setName("тархун");
        saved.add(new TagModel().setName("пепси").setCount(0).setId(0L));

        //act
        List<TagModel> actual = repository.updateTagList(saved);
        List<TagModel> expected = new ArrayList<>(repository.findAll());

        //assert
        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to replace empty tag list returns empty list")
    void updateTagListEmptyList() {
        //arrange
        List<TagModel> expected = new ArrayList<>();

        //act
        List<TagModel> actual = repository.updateTagList(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Search for models by names returns collection of models ")
    void findByNamesReturnsCorrectData() {
        //arrange
        List<TagModel> models = TestModel.generateTags();
        List<TagModel> expected = repository.saveTagList(models);

        //act
        Collection<TagModel> actual = repository.findByNames(models);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Search for models by names if names not exists in table ")
    void findByNamesIfNameNotExistInTable() {
        //arrange
        List<TagModel> models = new ArrayList<>();
        models.add(TestModel.generateTagModel());
        repository.saveTagList(models);
        List<TagModel> expected = new ArrayList<>();
        models.get(0).setName("some_tag");

        //act
        Collection<TagModel> actual = repository.findByNames(models);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to find by names in empty table returns empty collection")
    void findByNamesEmptyTable() {
        Collection<TagModel> models = repository.findAll();

        assertTrue(models.size() == 0);
    }

    @Test
    @DisplayName("Attempt to save by transaction empty tag list returns empty list")
    void saveByTransactionEmptyCollection() {
        //arrange
        List<TagModel> expected = new ArrayList<>();

        //act
        List<TagModel> actual = repository.saveUpdateByTransaction(expected, 1L);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("find by transaction returns the correct models")
    void findByTransactionCorrectModelsReturns() {
        //arrange
        TransactionModel transaction = TestModel.generateTransactionModel();
        TransactionModel saved = new TransactionRepositoryImpl().save(transaction);
        List<TagModel> expected = saved.getTags();

        //act
        List<TagModel> actual = repository.findByTransaction(saved.getId());

        //assert
        assertTrue(actual.size() > 0);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to find by transaction without tags returns empty list")
    void findByTransactionEmptyTags() {
        //arrange
        TransactionModel transaction = TestModel.generateTransactionModel().setTags(new ArrayList<>());
        TransactionModel saved = new TransactionRepositoryImpl().save(transaction);

        //act
        List<TagModel> actual = repository.findByTransaction(saved.getId());

        //assert
        assertTrue(actual.size() == 0);
    }

    @Test
    @DisplayName("Find all returns only unique names")
    void findALLOnlyUniqueNamesInDB() {
        //arrange
        TransactionModel transaction = TestModel.generateTransactionModel();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();

        transactionRepository.save(transaction);
        transaction.setId(null);
        transactionRepository.save(transaction); // re-saving with the same tags

        //act
        Collection<TagModel> actual = repository.findAll();

        //assert
        assertTrue(transaction.getTags().size() == actual.size());
    }
}