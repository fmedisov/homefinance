package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.common.utils.ModelUtils;
import ru.medisov.home_finance.service.*;
import ru.medisov.home_finance.web.view.TransactionView;

import java.util.ArrayList;

public class TransactionViewToModelConverter implements Converter<TransactionView, TransactionModel> {

    private final CategoryService categoryService;
    private final CurrencyService currencyService;
    private final AccountService accountService;

    @Autowired
    //todo implement without constructor
    public TransactionViewToModelConverter(CategoryService categoryService, CurrencyService currencyService, AccountService accountService) {
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.accountService = accountService;
    }

    public TransactionModel convert(TransactionView transactionView) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel
                .setId(transactionView.getId())
                .setName(transactionView.getName())
                .setAmount(transactionView.getAmount())
                .setDateTime(ModelUtils.parseDateTime(transactionView.getDateTime()))
                .setAccount(getAccount(transactionView.getAccount()))
                .setCategory(getCategory(transactionView.getCategory()))
                .setTransactionType(TransactionType.findByName(transactionView.getTransactionType()).orElse(null))
                //todo переписать нормально
                .setTags(new ArrayList<>());

        return transactionModel;
    }

    private CategoryTransactionModel getCategory(String category) {
        CategoryTransactionModel defaultValue = null;
        try {
            return categoryService.findByName(category).orElse(defaultValue);
        } catch (HomeFinanceServiceException e) {
            return defaultValue;
        }
    }

    private AccountModel getAccount(String account) {
        AccountModel defaultValue = null;
        try {
            return accountService.findByName(account).orElse(defaultValue);
        } catch (HomeFinanceServiceException e) {
            return defaultValue;
        }
    }
}

//.setId(transactionView.getId())
//        .setName(transactionView.getName())
//        .setAmount(transactionView.getAmount())
//        .setDateTime(transactionModel.getDateTime())
//        .setAccount(new AccountViewToModelConverter(currencyService).convert(transactionView.getAccount()))
//        .setCategory(new CategoryViewToModelConverter(categoryService).convert(transactionView.getCategory()))
//        .setTransactionType(TransactionType.findByName(transactionView.getTransactionType()).orElse(null))
//        //todo implement Tag Service
//        .setTags(transactionView.getTags().stream().map(tag -> new TagModel().setName(tag)).collect(Collectors.toList()));