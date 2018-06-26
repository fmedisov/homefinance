package ru.medisov.home_finance.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest extends CommonServiceTest {
    private TestModelGenerator generator = new TestModelGenerator();

    @Mock
    private TransactionRepository repositoryMock;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Search by name for an existing transaction. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        super.findByNameIfExistsCorrectModelReturned(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent transaction throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        super.findByNameIfNotExists(repositoryMock, generator, TransactionModel.class, transactionService);

    }

    @Test
    @DisplayName("Search for all transaction models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Attempt to search transaction models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, transactionService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        super.saveIncorrectModelValidationNotAccepted(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, generator, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        super.updateIncorrectModelCausesException(repositoryMock, generator, TransactionModel.class, transactionService);
    }
}
