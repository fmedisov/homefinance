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
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.common.utils.MoneyUtils;
import ru.medisov.home_finance.dao.repository.TransactionRepository;
import ru.medisov.home_finance.service.config.ServiceConfiguration;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ServiceConfiguration.class})
class TransactionServiceTest extends CommonServiceTest {

    @Mock
    private TransactionRepository repositoryMock;

    @Mock
    private CategoryService categoryServiceMock;

    @InjectMocks
    @Autowired
    private TransactionService transactionService;

    @Test
    @DisplayName("Search by name for an existing transaction. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        //arrange
        TransactionModel expectedModel = TestModel.generateTransactionModel();
        expectedModel.setId(1L);
        Optional<TransactionModel> expected = Optional.of(expectedModel);
        when(repositoryMock.findByName(expectedModel.getName())).thenReturn(expected);

        //act
        Optional<TransactionModel> actual = transactionService.findByName(expectedModel.getName());

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).findByName(expectedModel.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent transaction throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        TransactionModel model = TestModel.generateTransactionModel();
        model.setId(1L);
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), transactionService.findByName(model.getName()));
    }

    @Test
    @DisplayName("Search for all transaction models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repositoryMock, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Attempt to search transaction models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repositoryMock, transactionService);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repositoryMock, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repositoryMock, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        super.saveCorrectModelSuccessfulValidation(repositoryMock, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Attempt to save an model without date throws HomeFinanceServiceException. Validation not accepted")
    void saveModelWithoutDateValidationNotAccepted() throws HomeFinanceServiceException {
        TransactionModel model = TestModel.generateTransactionModel().setDateTime(null);

        assertThrows(HomeFinanceServiceException.class, () -> transactionService.save(model));
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("Attempt to save an model with incorrect date throws HomeFinanceServiceException. Validation not accepted")
    void saveWithIncorrectDateValidationNotAccepted() throws HomeFinanceServiceException {
        TransactionModel model = TestModel.generateTransactionModel().setDateTime(LocalDateTime.now().withYear(1969));

        assertThrows(HomeFinanceServiceException.class, () -> transactionService.save(model));
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("Attempt to save an model with null amount throws HomeFinanceServiceException. Validation not accepted")
    void saveNullAmountValidationNotAccepted() throws HomeFinanceServiceException {
        TransactionModel model = TestModel.generateTransactionModel().setAmount(null);

        assertThrows(HomeFinanceServiceException.class, () -> transactionService.save(model));
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        super.updateCorrectModelSameModelReturned(repositoryMock, TransactionModel.class, transactionService);
    }

    @Test
    @DisplayName("Search for transaction models by period returns collection of models ")
    void findByPeriodExistsOneEntry() {
        //arrange
        TransactionModel expectedModel = TestModel.generateTransactionModel();
        expectedModel.setId(1L);
        Collection<TransactionModel> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.findByPeriod(any(), any())).thenReturn(expected);

        //act
        Collection<TransactionModel> actual = transactionService.findByPeriod(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).findByPeriod(any(), any());
    }

    @Test
    @DisplayName("Search for transaction models by period with null dates returns all data in the table")
    void findByPeriodNullDatesReturnsAllData() {
        //arrange
        TransactionModel expectedModel = TestModel.generateTransactionModel();
        expectedModel.setId(1L);
        Collection<TransactionModel> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.findByPeriod(TestModel.getDateFrom(null), TestModel.getUpToDate(null))).thenReturn(expected);

        //act
        Collection<TransactionModel> actual = transactionService.findByPeriod(null, null);

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).findByPeriod(TestModel.getDateFrom(null), TestModel.getUpToDate(null));
    }

    @Test
    @DisplayName("Attempt to search transaction models by period in empty table returns empty collection")
    void findByPeriodEmptyTable() {
        Collection<TransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.findByPeriod(any(), any())).thenReturn(emptyCollection);

        assertEquals(new ArrayList<TransactionModel>(), transactionService.findByPeriod(LocalDateTime.MIN, LocalDateTime.MAX));
    }

    @Test
    @DisplayName("Search for income transaction models by period returns collection of models ")
    void incomeByPeriodExistsOneEntry() {
        //arrange
        TransactionModel expectedModel = TestModel.generateTransactionModel().setTransactionType(TransactionType.INCOME);
        expectedModel.setId(1L);
        Collection<TransactionModel> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.incomeByPeriod(any(), any())).thenReturn(expected);

        //act
        Collection<TransactionModel> actual = transactionService.incomeByPeriod(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).incomeByPeriod(any(), any());
    }

    @Test
    @DisplayName("Attempt to search income transaction models by period in empty table returns empty collection")
    void incomeByPeriodEmptyTable() {
        Collection<TransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.incomeByPeriod(any(), any())).thenReturn(emptyCollection);

        assertEquals(new ArrayList<TransactionModel>(), transactionService.incomeByPeriod(LocalDateTime.MIN, LocalDateTime.MAX));
    }

    @Test
    @DisplayName("Search for expense transaction models by period returns collection of models ")
    void expenseByPeriodExistsOneEntry() {
        //arrange
        TransactionModel expectedModel = TestModel.generateTransactionModel().setTransactionType(TransactionType.EXPENSE);
        expectedModel.setId(1L);
        Collection<TransactionModel> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.expenseByPeriod(any(), any())).thenReturn(expected);

        //act
        Collection<TransactionModel> actual = transactionService.expenseByPeriod(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).expenseByPeriod(any(), any());
    }

    @Test
    @DisplayName("Attempt to search expense transaction models by period in empty table returns empty collection")
    void expenseByPeriodEmptyTable() {
        Collection<TransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.expenseByPeriod(any(), any())).thenReturn(emptyCollection);

        assertEquals(new ArrayList<TransactionModel>(), transactionService.expenseByPeriod(LocalDateTime.MIN, LocalDateTime.MAX));
    }

    @Test
    @DisplayName("Search for transaction models by category returns collection of models ")
    void findByCategoryExistsOneEntry() {
        //arrange
        TransactionModel expectedModel = TestModel.generateTransactionModel();
        expectedModel.setId(1L);
        Collection<TransactionModel> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.findByCategory(any())).thenReturn(expected);

        //act
        Collection<TransactionModel> actual = transactionService.findByCategory(expectedModel.getCategory());

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).findByCategory(any());
    }

    @Test
    @DisplayName("Attempt to search transaction models by category in empty table returns empty collection")
    void findByCategoryEmptyTable() {
        Collection<TransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.findByCategory(any())).thenReturn(emptyCollection);

        assertEquals(new ArrayList<TransactionModel>(), transactionService.findByCategory(TestModel.generateCategoryModel().setId(1L)));
    }

    @Test
    @DisplayName("Attempt to calculate sum by period excluding categories in empty table returns zero result")
    void sumByPeriodNoCategoriesEmptyTable() {
        //arrange
        Collection<TransactionModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.incomeByPeriod(any(), any())).thenReturn(emptyCollection);
        when(repositoryMock.expenseByPeriod(any(), any())).thenReturn(emptyCollection);
        Map<String, IncomeExpense> expected = new HashMap<>();
        expected.put("Без учета категорий", defaultIncomeExpense());

        //act
        Map<String, IncomeExpense> actual = transactionService.sumByPeriodNoCategories(LocalDateTime.MIN, LocalDateTime.MAX);

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).incomeByPeriod(any(), any());
        verify(repositoryMock, times(1)).expenseByPeriod(any(), any());
    }

    @Test
    @DisplayName("Calculate sum by period excluding categories returns non zero result")
    void sumByPeriodNoCategoriesReturnsNonZeroResult() {
        //arrange
        Collection<TransactionModel> incomeTransactions = TestModel.generateIncomeTransactions();
        Collection<TransactionModel> expenseTransactions = TestModel.generateExpenseTransactions();
        when(repositoryMock.incomeByPeriod(any(), any())).thenReturn(incomeTransactions);
        when(repositoryMock.expenseByPeriod(any(), any())).thenReturn(expenseTransactions);
        Map<String, IncomeExpense> expected = new HashMap<>();
        expected.put("Без учета категорий",
                new IncomeExpense().setExpense(MoneyUtils.inBigDecimal(12500.53))
                                                                .setIncome(MoneyUtils.inBigDecimal(50000d))
        );

        //act
        Map<String, IncomeExpense> actual = transactionService.sumByPeriodNoCategories(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).incomeByPeriod(any(), any());
        verify(repositoryMock, times(1)).expenseByPeriod(any(), any());
    }

    @Test
    @DisplayName("Attempt to calculate sum by period and categories in empty table returns default data")
    void sumByPeriodByCategoriesEmptyTable() {
        //arrange
        Collection<CategoryTransactionModel> emptyCategories = new ArrayList<>();
        when(categoryServiceMock.findAll()).thenReturn(emptyCategories);
        Map<CategoryTransactionModel, IncomeExpense> expected = new HashMap<>();
        expected.put(null, defaultIncomeExpense());

        //act
        Map<CategoryTransactionModel, IncomeExpense> actual = transactionService.sumByPeriodByCategories(LocalDateTime.MIN, LocalDateTime.MAX);

        //assert
        assertEquals(expected, actual);
        verify(categoryServiceMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Calculate sum by period and categories returns non zero result")
    void sumByPeriodByCategoriesReturnsNonZeroResult() {
        //arrange
        Collection<CategoryTransactionModel> categories = TestModel.generateCategoryGroup();
        when(categoryServiceMock.findAll()).thenReturn(categories);
        Collection<TransactionModel> allTransactions = TestModel.generateTransactionGroup();
        when(repositoryMock.findByPeriod(any(), any())).thenReturn(allTransactions);
        Map<CategoryTransactionModel, IncomeExpense> expected = getIncomeExpenseByCategories(allTransactions, categories);

        //act
        Map<CategoryTransactionModel, IncomeExpense> actual = transactionService.sumByPeriodByCategories(LocalDateTime.now().minusYears(1000), LocalDateTime.now());

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).findByPeriod(any(), any());
        verify(categoryServiceMock, times(1)).findAll();
    }

    private Map<CategoryTransactionModel, IncomeExpense> getIncomeExpenseByCategories(Collection<TransactionModel> transactions,
                                                                                      Collection<CategoryTransactionModel> categories) {
        Map<CategoryTransactionModel, IncomeExpense> result = new HashMap<>();

        for (CategoryTransactionModel category : categories) {
            final BigDecimal income = getAmountByCategory(transactions, category, TransactionType.INCOME);
            final BigDecimal expense = getAmountByCategory(transactions, category, TransactionType.EXPENSE);

            result.put(category, new IncomeExpense().setExpense(expense).setIncome(income));
        }

        result.putAll(withoutCategories());

        return result;
    }

    private Map<CategoryTransactionModel, IncomeExpense> withoutCategories() {
        Map<CategoryTransactionModel, IncomeExpense> result = new HashMap<>();
        result.put(null, defaultIncomeExpense().setIncome(MoneyUtils.inBigDecimal(20000d)));
        return result;
    }

    private IncomeExpense defaultIncomeExpense() {
        return new IncomeExpense().setIncome(BigDecimal.ZERO).setExpense(BigDecimal.ZERO);
    }

    private BigDecimal getAmountByCategory(Collection<TransactionModel> transactions, CategoryTransactionModel category, TransactionType type) {
        final BigDecimal[] result = {BigDecimal.ZERO};
        transactions.stream()
                .filter(t -> category.equals(t.getCategory()) && t.getTransactionType().equals(type))
                .forEach(t -> result[0] = result[0].add(t.getAmount()));

        return result[0];
    }
}
