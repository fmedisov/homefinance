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
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.dao.repository.AccountRepository;
import ru.medisov.home_finance.service.config.ServiceConfiguration;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ServiceConfiguration.class})
class AccountServiceTest extends CommonServiceTest {

    @Mock
    private AccountRepository repositoryMock;

    @InjectMocks
    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("Search by name for an existing account. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        //arrange
        AccountModel expectedModel = TestModel.generateAccountModel();
        expectedModel.setId(1L);
        Optional<AccountModel> expected = Optional.of(expectedModel);
        when(repositoryMock.findByName(expectedModel.getName())).thenReturn(expected);

        //act
        Optional<AccountModel> actual = accountService.findByName(expectedModel.getName());

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).findByName(expectedModel.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent account returns Optional.empty()")
    void findByNameIfNotExists() {
        AccountModel model = TestModel.generateAccountModel();
        model.setId(1L);
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), accountService.findByName(model.getName()));
    }

    @Test
    @DisplayName("Search for all account models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to search account models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, accountService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        super.saveIncorrectModelValidationNotAccepted(repositoryMock, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, AccountModel.class, accountService);
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        super.updateIncorrectModelCausesException(repositoryMock, AccountModel.class, accountService);
    }
}
