package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.common.model.TransactionModel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CommonRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void truncateAllTables() {
        String sqlQuery =
                "SET FOREIGN_KEY_CHECKS=0; " +
                        "TRUNCATE TABLE `currency_tbl`; " +
                        "TRUNCATE TABLE `account_tbl`; " +
                        "TRUNCATE TABLE `category_tbl`; " +
                        "TRUNCATE TABLE `transaction_tbl`; " +
                        "TRUNCATE TABLE `tag_relation_tbl`; " +
                        "TRUNCATE TABLE `tag_tbl`; " +
                        "SET FOREIGN_KEY_CHECKS=1;";
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(sqlQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T extends TagModel> void saveCorrectModelNonNullIdReturned(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        T model = generateModelWithSavedFields(aModelClass);

        T actual = repository.save(model);

        assertNotNull(actual.getId());
    }

    public <T extends TagModel> void findByNameIfExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        T expected = repository.save(generateModelWithSavedFields(aModelClass));

        //act
        T actual = null;
        try {
            actual = repository.findByName(expected.getName()).orElse(aModelClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> void findByNameIfNotExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        Optional<T> expected = Optional.empty();
        T model = TestModel.generateModel(aModelClass);

        //act
        Optional<T> actual = repository.findByName(model.getName());

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> void findAllExistsOneEntry(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        int expectedSize = 1;
        T model = generateModelWithSavedFields(aModelClass);
        T expectedModel = repository.save(model);

        //act
        Collection<T> actual = repository.findAll();

        //assert
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedModel));
    }

    public <T extends TagModel> void findAllEmptyTable(ExtendedRepository<T, Long> repository) {
        Collection<T> models = repository.findAll();

        assertTrue(models.size() == 0);
    }

    public <T extends TagModel> void removeExistingEntryReturnsTrue(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        T model = repository.save(generateModelWithSavedFields(aModelClass));

        assertTrue(repository.remove(model.getId()));
        assertEquals(0, repository.findAll().size());
    }

    public <T extends TagModel> void removeIfNotExistsReturnsFalse(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        T model = TestModel.generateModel(aModelClass);
        assertFalse(repository.remove(model.getId()));
    }

    public <T extends TagModel> void findByIdIfExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        T expected = repository.save(generateModelWithSavedFields(aModelClass));

        //act
        T actual = null;
        try {
            actual = repository.findById(expected.getId()).orElse(aModelClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> void findByIdIfNotExistsInDatabase(ExtendedRepository<T, Long> repository, Class<T> aModelClass) {
        //arrange
        Optional<T> expected = Optional.empty();
        T model = TestModel.generateModel(aModelClass);

        //act
        Optional<T> actual = repository.findById(model.getId());

        //assert
        assertEquals(expected, actual);
    }

    public <T extends TagModel> T generateModelWithSavedFields(Class<T> aModelClass) {
        T model = TestModel.generateModel(aModelClass);

        Object obj = null;

        switch (aModelClass.getSimpleName()) {
            case "AccountModel":
                AccountModel accountModel = (AccountModel) model;
                accountModel.setCurrencyModel(currencyRepository.save(accountModel.getCurrencyModel()));
                obj = accountModel;
                break;
            case "CurrencyModel":
                obj = model;
                break;
            case "CategoryTransactionModel":
                obj = model;
                break;
            case "TransactionModel":
                TransactionModel transactionModel = (TransactionModel) model;
                transactionModel.setCategory(categoryRepository.saveWithParents(transactionModel.getCategory()));
                CurrencyModel currency = currencyRepository.save(transactionModel.getAccount().getCurrencyModel());
                AccountModel account = transactionModel.getAccount().setCurrencyModel(currency);
                transactionModel.setAccount(accountRepository.save(account));
                obj = transactionModel;
                break;
            case "TagModel":
                obj = model;
                break;
        }

        return (T) obj;
    }
}
