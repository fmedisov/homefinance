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
class TransactionServiceTest {
    private TestModelGenerator generator = new TestModelGenerator();

    @Mock
    private TransactionRepository repositoryMock;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Search by name for an existing transaction. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        TransactionModel returnModel = generator.generateTransactionModel();
        when(repositoryMock.findByName(returnModel.getName())).thenReturn(Optional.of(returnModel));

        assertEquals(Optional.of(returnModel), transactionService.findByName(returnModel.getName()));
        verify(repositoryMock, times(1)).findByName(returnModel.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent transaction throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        TransactionModel model = generator.generateTransactionModel();
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> transactionService
                .findByName(model.getName()));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search for all transaction models returns collection of models ")
    void findAllExistsOneEntry() {
        TransactionModel returnModel = generator.generateTransactionModel();
        Collection<TransactionModel> models = new ArrayList<>();
        models.add(returnModel);
        when(repositoryMock.findAll()).thenReturn(models);

        Collection<TransactionModel> actual = transactionService.findAll();
        assertEquals(models, actual);
        assertTrue(actual.contains(returnModel));
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Attempt to search transaction models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<TransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(emptyCollection);
        assertEquals(new ArrayList<TransactionModel>(), transactionService.findAll());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        TransactionModel model = generator.generateTransactionModel();
        when(repositoryMock.remove(model.getId())).thenReturn(true);
        assertTrue(transactionService.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        TransactionModel model = generator.generateTransactionModel();
        when(repositoryMock.remove(model.getId())).thenReturn(false);
        assertFalse(transactionService.remove(model.getId()));
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        TransactionModel returnModel = generator.generateTransactionModel();

        when(repositoryMock.save(any())).thenReturn(returnModel);

        assertNotNull(transactionService);
        assertEquals(returnModel, transactionService.save(returnModel));

        verify(repositoryMock, times(1)).save(any());
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        TransactionModel model = generator.generateTransactionModel();
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> transactionService
                                                                        .save(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        TransactionModel returnModel = generator.generateTransactionModel();

        when(repositoryMock.update(any())).thenReturn(returnModel);
        assertEquals(returnModel, transactionService.update(returnModel));
        verify(repositoryMock, times(1)).update(any());
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        TransactionModel model = generator.generateTransactionModel();
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> transactionService.update(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).update(any());
    }
}
