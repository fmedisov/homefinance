package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.TagModel;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest extends CommonRepositoryTest implements RepositoryTest {
    private TestModelGenerator generator = new TestModelGenerator();
    private TagRepository repository = new TagRepositoryImpl();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonZeroIdReturned() {
        TagModel tagModel = generator.generateTagModel();
        TagModel changed = repository.save(tagModel);
        assertTrue(changed.getId() != null);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        TagModel tagModel = generator.generateTagModel();
        TagModel modelWithLongName = tagModel.setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongName));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        TagModel tagModel = generator.generateTagModel();
        TagModel changed = repository.save(tagModel);
        TagModel found = repository.findByName(tagModel.getName()).orElse(new TagModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        TagModel tagModel = generator.generateTagModel();
        Optional<TagModel> found = repository.findByName(tagModel.getName());
        assertEquals(Optional.empty(), found);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        TagModel tagModel = generator.generateTagModel();
        TagModel model = repository.save(tagModel);
        Collection<TagModel> models = repository.findAll();
        assertEquals(1, models.size());
        assertTrue(models.contains(model));
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<TagModel> models = repository.findAll();
        assertEquals(0, models.size());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        TagModel tagModel = generator.generateTagModel();
        TagModel model = repository.save(tagModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        TagModel tagModel = generator.generateTagModel();
        assertFalse(repository.remove(tagModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        TagModel tagModel = generator.generateTagModel();
        TagModel changed = repository.save(tagModel).setName("@проезд").setCount(2);
        TagModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new TagModel()));
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        TagModel tagModel = generator.generateTagModel();
        TagModel modelWithLongName = repository.save(tagModel).setName(generator.getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        TagModel tagModel = generator.generateTagModel();
        TagModel changed = repository.save(tagModel);
        TagModel found = repository.findById(tagModel.getId()).orElse(new TagModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        TagModel tagModel = generator.generateTagModel();
        Optional<TagModel> found = repository.findById(tagModel.getId());
        assertEquals(found, Optional.empty());
    }
}