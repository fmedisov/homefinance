package ru.medisov.home_finance.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.CurrencyRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest extends CommonServiceTest {

    @Mock
    private CurrencyRepository repositoryMock;

    @InjectMocks
    private CurrencyService currencyService = new CurrencyServiceImpl();

    @Test
    @DisplayName("Search by name for an existing currency. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        super.findByNameIfExistsCorrectModelReturned(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent currency throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        super.findByNameIfNotExists(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Search for all currency models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Attempt to search currency models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, currencyService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        super.saveIncorrectModelValidationNotAccepted(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        super.updateIncorrectModelCausesException(repositoryMock, CurrencyModel.class, currencyService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Verification not accepted")
    void saveExistingModelVerificationNotAccepted() throws HomeFinanceServiceException {
        CurrencyModel model = TestModel.generateCurrencyModel();
        String name = model.getName();
        CurrencyModel returnModel = new CurrencyModel().setName(name);
        when(repositoryMock.findByName(name)).thenReturn(Optional.of(returnModel));

        assertThrows(HomeFinanceServiceException.class, () -> currencyService.save(model));
        verify(repositoryMock, never()).save(any());
    }
}
