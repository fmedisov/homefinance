package ru.medisov.home_finance.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.AccountRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends CommonServiceTest {
    private TestModelGenerator generator = new TestModelGenerator();

    @Mock
    private AccountRepository repositoryMock;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Search by name for an existing account. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        super.findByNameIfExistsCorrectModelReturned(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent account throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        super.findByNameIfNotExists(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Search for all account models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to search account models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, accountService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        super.saveIncorrectModelValidationNotAccepted(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, generator, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        super.updateIncorrectModelCausesException(repositoryMock, generator, AccountModel.class, accountService);
    }
}
