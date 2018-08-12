package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.utils.MoneyUtils;
import ru.medisov.home_finance.dao.config.DaoConfiguration;
import ru.medisov.home_finance.dao.exception.HomeFinanceDaoException;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfiguration.class})
class AccountRepositoryTest extends CommonRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private AccountRepository repository;

    @Test
    @DisplayName("Save correct Model to database")
    void saveCorrectModelNonNullIdReturned() {
        super.saveCorrectModelNonNullIdReturned(repository, AccountModel.class);
    }

    @Test
    @DisplayName("Attempt to save a Model without account type throws HomeFinanceDaoException")
    void saveWithoutAccTypeCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = super.generateModelWithSavedFields(AccountModel.class);
        AccountModel withoutAccType = accountModel.setAccountType(null);

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(withoutAccType));
    }

    @Test
    @DisplayName("Attempt to save a Model with too long name throws HomeFinanceDaoException")
    void saveWithLongNameCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = super.generateModelWithSavedFields(AccountModel.class);
        AccountModel withLongName = accountModel.setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.save(withLongName));
    }

    @Test
    @DisplayName("Search by name for an existing model in the database")
    void findByNameIfExistsInDatabase() {
        super.findByNameIfExistsInDatabase(repository, AccountModel.class);
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent model returns Optional.empty()")
    void findByNameIfNotExistsInDatabase() {
        super.findByNameIfNotExistsInDatabase(repository, AccountModel.class);
    }

    @Test
    @DisplayName("Search for all models returns collection of models ")
    void findAllExistsOneEntry() {
        super.findAllExistsOneEntry(repository, AccountModel.class);
    }

    @Test
    @DisplayName("Attempt to search models in empty table returns empty collection")
    void findAllEmptyTable() {
        super.findAllEmptyTable(repository);
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        super.removeExistingEntryReturnsTrue(repository, AccountModel.class);
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        super.removeIfNotExistsReturnsFalse(repository, AccountModel.class);
}

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        //arrange
        CurrencyModel currencyModel = currencyRepository.save(new CurrencyModel()
                                            .setName("US dollar").setCode("USD").setSymbol("$"));

        AccountModel saved = repository.save(super.generateModelWithSavedFields(AccountModel.class));
        AccountModel expected = saved
                .setAccountType(AccountType.CREDIT_CARD).setName("Citibank Card")
                .setCurrencyModel(currencyModel).setAmount(MoneyUtils.inBigDecimal(12345));

        //act
        AccountModel actual = repository.update(expected);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Attempt to update model without account type throws HomeFinanceDaoException")
    void updateWithoutAccTypeCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = super.generateModelWithSavedFields(AccountModel.class);
        AccountModel withoutAccType = repository.save(accountModel).setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(withoutAccType));
    }

    @Test
    @DisplayName("Attempt to update model with too long name throws HomeFinanceDaoException")
    void updateWithTooLongNameCausesException() throws HomeFinanceDaoException {
        AccountModel accountModel = super.generateModelWithSavedFields(AccountModel.class);
        AccountModel withLongName = repository.save(accountModel).setName(TestModel.getLongName());

        assertThrows(HomeFinanceDaoException.class, () -> repository.update(withLongName));
    }

    @Test
    @DisplayName("Search by id for an existing model in the database")
    void findByIdIfExistsInDatabase() {
        super.findByIdIfExistsInDatabase(repository, AccountModel.class);
    }

    @Test
    @DisplayName("Attempt to search by id for a non-existent model returns Optional.empty()")
    void findByIdIfNotExistsInDatabase() {
        super.findByIdIfNotExistsInDatabase(repository, AccountModel.class);
    }
}