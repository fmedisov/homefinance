package ru.medisov.home_finance.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;
import ru.medisov.home_finance.service.config.ServiceConfiguration;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ServiceConfiguration.class})
class CategoryServiceTest extends CommonServiceTest {

    @Mock
    private CategoryRepository repositoryMock;

    @InjectMocks
    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("Search by name for an existing category. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        //arrange
        CategoryTransactionModel expectedModel = TestModel.generateCategoryModel();
        expectedModel.setId(1L);
        Optional<CategoryTransactionModel> expected = Optional.of(expectedModel);
        when(repositoryMock.findByName(expectedModel.getName())).thenReturn(expected);

        //act
        Optional<CategoryTransactionModel> actual = categoryService.findByName(expectedModel.getName());

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).findByName(expectedModel.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent Category returns Optional.empty()")
    void findByNameIfNotExists() {
        CategoryTransactionModel model = TestModel.generateCategoryModel();
        model.setId(1L);
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), categoryService.findByName(model.getName()));
    }

    @Test
    @DisplayName("Search for all Category models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to search Category models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, categoryService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        super.saveIncorrectModelValidationNotAccepted(repositoryMock, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        super.updateIncorrectModelCausesException(repositoryMock, CategoryTransactionModel.class, categoryService);
    }
}
