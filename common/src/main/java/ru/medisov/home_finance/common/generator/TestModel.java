package ru.medisov.home_finance.common.generator;

import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.common.utils.MoneyUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class TestModel {

    public static CurrencyModel generateCurrencyModel() {
        return new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
    }

    public static CategoryTransactionModel generateCategoryModel() {
        return new CategoryTransactionModel().setName("Проезд").setParent(null);
    }

    public static CategoryTransactionModel generateCategoryWithParents() {
        CategoryTransactionModel mainParent = new CategoryTransactionModel().setName("Проезд");
        CategoryTransactionModel parent = new CategoryTransactionModel().setName("Авто");
        CategoryTransactionModel category = new CategoryTransactionModel().setName("Бензин");
        parent.setParent(mainParent);
        category.setParent(parent);
        return category;
    }

    public static AccountModel generateAccountModel() {
        CurrencyModel currencyModel = new CurrencyModel().setName("Боливиано").setCode("BOB").setSymbol("$");
        BigDecimal amount = MoneyUtils.inBigDecimal(50000L);
        return new AccountModel().setCurrencyModel(currencyModel).setAccountType(AccountType.CASH)
                .setName("Кошелек").setAmount(amount);
    }

    public static TagModel generateTagModel() {
        return new TagModel().setName("#проэзд").setCount(1L);
    }

    public static Set<TagModel> generateTags() {
        Set<TagModel> models = new HashSet<>();
        models.add(new TagModel().setName("#отпуск"));
        models.add(new TagModel().setName("#проезд"));
        models.add(new TagModel().setName("#авто"));
        return models;
    }

    public static TransactionModel generateTransactionModel() {
        AccountModel accountModel = generateAccountModel();
        CategoryTransactionModel category = generateCategoryModel();
        Set<TagModel> tags = generateTags();

        return new TransactionModel().setTransactionType(TransactionType.EXPENSE)
                .setAccount(accountModel).setCategory(category).setDateTime(LocalDateTime.now())
                .setAmount(MoneyUtils.inBigDecimal(3000L)).setName("Бензин 95")
                .setTags(tags);
    }

    public static String getLongName() {
        return "Long long long long long long long long long long long long long long long long long long long model name";
    }

    public static <T> T generateModel(Class<T> aClass) {
        Object obj = null;
        switch (aClass.getSimpleName()) {
            case "AccountModel":
                obj = generateAccountModel();
                break;
            case "CurrencyModel":
                obj = generateCurrencyModel();
                break;
            case "CategoryTransactionModel":
                obj = generateCategoryModel();
                break;
            case "TransactionModel":
                obj = generateTransactionModel();
                break;
            case "TagModel":
                obj = generateTagModel();
                break;
        }

        return (T) obj;
    }

    public static Collection<TransactionModel> generateIncomeTransactions() {
        Collection<TransactionModel> result = new ArrayList<>();
        result.add(
                generateTransactionModel()
                        .setCategory(categorySalary())
                        .setDateTime(LocalDateTime.now().minusYears(2))
                        .setAmount(MoneyUtils.inBigDecimal(30000d))
                        .setTransactionType(TransactionType.INCOME)
        );
        result.add(
                generateTransactionModel()
                        .setCategory(null)
                        .setDateTime(LocalDateTime.now().minusYears(1))
                        .setAmount(MoneyUtils.inBigDecimal(20000d))
                        .setTransactionType(TransactionType.INCOME)
        );

        return result;
    }

    public static Collection<TransactionModel> generateExpenseTransactions() {
        Collection<TransactionModel> result = new ArrayList<>();
        result.add(
                generateTransactionModel()
                        .setCategory(categoryPassage())
                        .setDateTime(LocalDateTime.now().minusDays(3))
                        .setAmount(MoneyUtils.inBigDecimal(10000L))
        );
        result.add(
                generateTransactionModel()
                        .setCategory(categoryRestaurant())
                        .setDateTime(LocalDateTime.now().minusDays(5))
                        .setAmount(MoneyUtils.inBigDecimal(2500.53))
        );

        return result;
    }

    public static Collection<TransactionModel> generateTransactionGroup() {
        Collection<TransactionModel> result = new ArrayList<>(generateIncomeTransactions());
        result.addAll(generateExpenseTransactions());
        return result;
    }

    public static Collection<CategoryTransactionModel> generateCategoryGroup() {
        return Arrays.asList(categorySalary(), categoryFood(), categoryRestaurant(), categoryPassage());
    }

    public static CategoryTransactionModel categorySalary() {
        return new CategoryTransactionModel().setName("Зарплата").setParent(null).setId(1L);
    }

    public static CategoryTransactionModel categoryFood() {
        return new CategoryTransactionModel().setName("Еда").setParent(null).setId(2L);
    }

    public static CategoryTransactionModel categoryRestaurant() {
        return new CategoryTransactionModel().setName("Ресторан").setParent(categoryFood()).setId(3L);
    }

    public static CategoryTransactionModel categoryPassage() {
        return new CategoryTransactionModel().setName("Проезд").setParent(null).setId(4L);
    }

    public static LocalDateTime getDateFrom(LocalDateTime dateFrom) {
        return dateFrom != null ? dateFrom : LocalDateTime.MIN;
    }

    public static LocalDateTime getUpToDate(LocalDateTime upToDate) {
        return upToDate != null ? upToDate : LocalDateTime.MAX;
    }
}
