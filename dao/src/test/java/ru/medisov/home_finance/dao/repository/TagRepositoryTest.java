package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.TagModel;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest extends AbstractRepositoryTest {
    private TagModel tagModel = getTagModel();
    private TagRepository repository = new TagRepository();

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonZeroIdReturned() {
        TagModel changed = repository.save(tagModel);
        assertTrue(changed.getId() != 0);
    }

    @Test
    @DisplayName("Attempt to save an incorrect Model to database throws HomeFinanceDaoException")
    void saveIncorrectModelCausesException() throws HomeFinanceDaoException {
        TagModel modelWithLongName = getTagModel().setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.save(modelWithLongName));
        assertNotNull(thrown.getMessage());

    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        TagModel changed = repository.save(tagModel);
        TagModel found = repository.findByName(tagModel.getName()).orElse(new TagModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        Optional<TagModel> found = repository.findByName(tagModel.getName());
        assertEquals(Optional.empty(), found);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
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
        TagModel model = repository.save(tagModel);
        assertTrue(repository.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        assertFalse(repository.remove(tagModel.getId()));
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        TagModel changed = repository.save(tagModel).setName("@проезд").setCount(2);
        TagModel updated = repository.update(changed);
        assertEquals(changed, repository.findByName(updated.getName()).orElse(new TagModel()));
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceDaoException")
    void updateIncorrectModelCausesException() throws HomeFinanceDaoException {
        TagModel modelWithLongName = repository.save(tagModel).setName(getLongName());
        Throwable thrown = assertThrows(HomeFinanceDaoException.class, () -> repository.update(modelWithLongName));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        TagModel changed = repository.save(tagModel);
        TagModel found = repository.findById(tagModel.getId()).orElse(new TagModel());
        assertEquals(changed, found);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        Optional<TagModel> found = repository.findById(tagModel.getId());
        assertEquals(found, Optional.empty());
    }
}