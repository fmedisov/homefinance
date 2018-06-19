package ru.medisov.home_finance.dao.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.dao.DaoConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbstractRepositoryTest {

    @BeforeAll
    static void initConfig() {
        DaoConfig.initConfig();
    }

    @BeforeEach
    public void truncateAllTables() {
        String sqlQuery =
                "SET FOREIGN_KEY_CHECKS=0; " +
                "TRUNCATE TABLE `currency_tbl`; " +
                "TRUNCATE TABLE `account_tbl`; " +
                "TRUNCATE TABLE `category_tbl`; " +
                "TRUNCATE TABLE `transaction_tbl`; " +
                "TRUNCATE TABLE `tag_tbl`; " +
                "TRUNCATE TABLE `tag_relation_tbl`; " +
                "SET FOREIGN_KEY_CHECKS=1;";
        executeSqlQuery(sqlQuery);
    }

    public void executeSqlQuery(String sqlQuery) {
        try (Connection connection = new DbConnectionBuilder().getConnection()) {
            connection.prepareStatement(sqlQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLongName() {
        return "Long long long long long long long long long long long long long long long long long long category name";
    }

    public CurrencyModel getCurrencyModel() {
        return new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
    }

    public BigDecimal getBaseAmount() {
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_CEILING);
    }

    public CategoryTransactionModel getCategoryModel() {
        return new CategoryTransactionModel().setName("Проезд").setParent(null);
    }

    public CategoryTransactionModel getCategoriesWithParents() {
        CategoryTransactionModel mainParent = new CategoryTransactionModel().setName("Проезд");
        CategoryTransactionModel parent = new CategoryTransactionModel().setName("Авто");
        CategoryTransactionModel category = new CategoryTransactionModel().setName("Бензин");
        parent.setParent(mainParent);
        category.setParent(parent);
        return category;
    }

    public AccountModel getAccountModel() {
        CurrencyModel currencyModel = new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
        BigDecimal amount = getBaseAmount().add(BigDecimal.valueOf(50000));
        return new AccountModel().setCurrencyModel(currencyModel).setAccountType(AccountType.CASH)
                .setName("Кошелек").setAmount(amount);
    }

    public TagModel getTagModel() {
        return new TagModel().setName("#проэзд").setCount(1);
    }

    public List<TagModel> getTags() {
        List<TagModel> models = new ArrayList<>();
        models.add(new TagModel().setName("#отпуск"));
        models.add(new TagModel().setName("#проезд"));
        models.add(new TagModel().setName("#авто"));
        return models;
    }
}
