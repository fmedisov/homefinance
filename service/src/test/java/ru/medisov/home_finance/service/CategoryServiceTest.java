package ru.medisov.home_finance.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest extends CommonServiceTest {
    private TestModelGenerator generator = new TestModelGenerator();

    @Mock
    private CategoryRepository repositoryMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Search by name for an existing category. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        super.findByNameIfExistsCorrectModelReturned(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent Category throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        super.findByNameIfNotExists(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Search for all Category models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to search Category models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, categoryService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        super.saveIncorrectModelValidationNotAccepted(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        super.updateIncorrectModelCausesException(repositoryMock, generator, CategoryTransactionModel.class, categoryService);
    }
}
