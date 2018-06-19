package ru.medisov.home_finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private TransactionModel model = getTransactionModel();

    @Mock
    private TransactionRepository repositoryMock;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void reset() {
        model = getTransactionModel();
    }

    @Test
    @DisplayName("Search by name for an existing transaction. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        TransactionModel returnModel = model;
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.of(returnModel));

        assertEquals(Optional.of(returnModel), transactionService.findByName(model.getName()));
        verify(repositoryMock, times(1)).findByName(model.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent transaction throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> transactionService
                .findByName(model.getName()));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search for all transaction models returns collection of models ")
    void findAllExistsOneEntry() {
        TransactionModel returnModel = model;
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
        when(repositoryMock.remove(model.getId())).thenReturn(true);
        assertTrue(transactionService.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        when(repositoryMock.remove(model.getId())).thenReturn(false);
        assertFalse(transactionService.remove(model.getId()));
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        TransactionModel returnModel = model;

        when(repositoryMock.save(any())).thenReturn(returnModel);

        assertNotNull(transactionService);
        assertEquals(returnModel, transactionService.save(model));

        verify(repositoryMock, times(1)).save(any());
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> transactionService
                                                                        .save(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        TransactionModel returnModel = model;

        when(repositoryMock.update(any())).thenReturn(returnModel);
        assertEquals(returnModel, transactionService.update(model));
        verify(repositoryMock, times(1)).update(any());
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> transactionService.update(model.setName(emptyName)));
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

    private CategoryTransactionModel getCategoryModel() {
        return new CategoryTransactionModel().setName("Проезд").setId(1);
    }

    public List<TagModel> getTags() {
        List<TagModel> models = new ArrayList<>();
        models.add(new TagModel().setName("#отпуск"));
        models.add(new TagModel().setName("#проезд"));
        models.add(new TagModel().setName("#авто"));
        return models;
    }

    private TransactionModel getTransactionModel() {
        AccountModel accountModel = getAccountModel();
        CategoryTransactionModel category = getCategoryModel();
        List<TagModel> tags = getTags();

        return new TransactionModel().setTransactionType(TransactionType.EXPENSE)
                .setAccount(accountModel).setCategory(category).setDateTime(LocalDateTime.now())
                .setAmount(getBaseAmount().add(BigDecimal.valueOf(3000))).setName("Бензин 95")
                .setTags(tags);
    }
}
