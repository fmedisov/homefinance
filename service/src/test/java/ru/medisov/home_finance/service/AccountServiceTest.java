package ru.medisov.home_finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private AccountModel model = getAccountModel();

    @Mock
    private Repository<AccountModel, Long> repositoryMock;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void reset() {
        model = getAccountModel();
    }

    @Test
    @DisplayName("Search by name for an existing account. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        AccountModel returnModel = model;
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.of(returnModel));

        assertEquals(Optional.of(returnModel), accountService.findByName(model.getName()));
        verify(repositoryMock, times(1)).findByName(model.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent account throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> accountService
                .findByName(model.getName()));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search for all account models returns collection of models ")
    void findAllExistsOneEntry() {
        AccountModel returnModel = model;
        Collection<AccountModel> models = new ArrayList<>();
        models.add(returnModel);
        when(repositoryMock.findAll()).thenReturn(models);

        Collection<AccountModel> actual = accountService.findAll();
        assertEquals(models, actual);
        assertTrue(actual.contains(returnModel));
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Attempt to search account models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<AccountModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(emptyCollection);
        assertEquals(new ArrayList<AccountModel>(), accountService.findAll());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        when(repositoryMock.remove(model.getId())).thenReturn(true);
        assertTrue(accountService.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        when(repositoryMock.remove(model.getId())).thenReturn(false);
        assertFalse(accountService.remove(model.getId()));
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        AccountModel returnModel = model;

        when(repositoryMock.save(any())).thenReturn(returnModel);

        assertNotNull(accountService);
        assertEquals(returnModel, accountService.save(model));

        verify(repositoryMock, times(1)).save(any());
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> accountService
                                                                        .save(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        AccountModel returnModel = model;

        when(repositoryMock.update(any())).thenReturn(returnModel);
        assertEquals(returnModel, accountService.update(model));
        verify(repositoryMock, times(1)).update(any());
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> accountService.update(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).update(any());
    }

    public BigDecimal getBaseAmount() {
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_CEILING);
    }

    public AccountModel getAccountModel() {
        CurrencyModel currencyModel = new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
        BigDecimal amount = getBaseAmount().add(BigDecimal.valueOf(50000));
        return new AccountModel().setCurrencyModel(currencyModel).setAccountType(AccountType.CASH)
                .setName("Кошелек").setAmount(amount);
    }
}
