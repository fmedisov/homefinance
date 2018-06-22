package ru.medisov.home_finance.common.generator;

import ru.medisov.home_finance.common.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestModelGenerator {

    public CurrencyModel generateCurrencyModel() {
        return new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
    }

    public CategoryTransactionModel generateCategoryModel() {
        return new CategoryTransactionModel().setName("Проезд").setParent(null);
    }

    public CategoryTransactionModel generateCategoryWithParents() {
        CategoryTransactionModel mainParent = new CategoryTransactionModel().setName("Проезд");
        CategoryTransactionModel parent = new CategoryTransactionModel().setName("Авто");
        CategoryTransactionModel category = new CategoryTransactionModel().setName("Бензин");
        parent.setParent(mainParent);
        category.setParent(parent);
        return category;
    }

    public AccountModel generateAccountModel() {
        CurrencyModel currencyModel = new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
        BigDecimal amount = getBaseAmount().add(BigDecimal.valueOf(50000));
        return new AccountModel().setCurrencyModel(currencyModel).setAccountType(AccountType.CASH)
                .setName("Кошелек").setAmount(amount);
    }

    public TagModel generateTagModel() {
        return new TagModel().setName("#проэзд").setCount(1);
    }

    public List<TagModel> generateTags() {
        List<TagModel> models = new ArrayList<>();
        models.add(new TagModel().setName("#отпуск"));
        models.add(new TagModel().setName("#проезд"));
        models.add(new TagModel().setName("#авто"));
        return models;
    }

    public TransactionModel generateTransactionModel() {
        AccountModel accountModel = generateAccountModel();
        CategoryTransactionModel category = generateCategoryModel();
        List<TagModel> tags = generateTags();

        return new TransactionModel().setTransactionType(TransactionType.EXPENSE)
                .setAccount(accountModel).setCategory(category).setDateTime(LocalDateTime.now())
                .setAmount(getBaseAmount().add(BigDecimal.valueOf(3000))).setName("Бензин 95")
                .setTags(tags);
    }

    public String getLongName() {
        return "Long long long long long long long long long long long long long long long long long long long model name";
    }

    public BigDecimal getBaseAmount() {
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_CEILING);
    }
}
