package ru.medisov.home_finance.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private TestModelGenerator generator = new TestModelGenerator();

    @Mock
    private CategoryRepository repositoryMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Search by name for an existing category. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        CategoryTransactionModel returnModel = generator.generateCategoryModel().setId(1);
        when(repositoryMock.findByName(returnModel.getName())).thenReturn(Optional.of(returnModel));

        assertEquals(Optional.of(returnModel), categoryService.findByName(returnModel.getName()));
        verify(repositoryMock, times(1)).findByName(returnModel.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent Category throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        CategoryTransactionModel model = generator.generateCategoryModel().setId(1);
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> categoryService
                .findByName(model.getName()));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search for all Category models returns collection of models ")
    void findAllExistsOneEntry() {
        CategoryTransactionModel returnModel = generator.generateCategoryModel().setId(1);
        Collection<CategoryTransactionModel> models = new ArrayList<>();
        models.add(returnModel);
        when(repositoryMock.findAll()).thenReturn(models);

        Collection<CategoryTransactionModel> actual = categoryService.findAll();
        assertEquals(models, actual);
        assertTrue(actual.contains(returnModel));
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Attempt to search Category models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<CategoryTransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(emptyCollection);
        assertEquals(new ArrayList<CategoryTransactionModel>(), categoryService.findAll());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        CategoryTransactionModel model = generator.generateCategoryModel().setId(1);
        when(repositoryMock.remove(model.getId())).thenReturn(true);
        assertTrue(categoryService.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        CategoryTransactionModel model = generator.generateCategoryModel().setId(1);
        when(repositoryMock.remove(model.getId())).thenReturn(false);
        assertFalse(categoryService.remove(model.getId()));
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        CategoryTransactionModel returnModel = generator.generateCategoryModel().setId(1);

        when(repositoryMock.save(any())).thenReturn(returnModel);

        assertNotNull(categoryService);
        assertEquals(returnModel, categoryService.save(returnModel));

        verify(repositoryMock, times(1)).save(any());
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        CategoryTransactionModel model = generator.generateCategoryModel().setId(1);
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> categoryService
                                                                        .save(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        CategoryTransactionModel returnModel = generator.generateCategoryModel().setId(1);

        when(repositoryMock.update(any())).thenReturn(returnModel);
        assertEquals(returnModel, categoryService.update(returnModel));
        verify(repositoryMock, times(1)).update(any());
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        CategoryTransactionModel model = generator.generateCategoryModel().setId(1);
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> categoryService.update(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).update(any());
    }
}
