package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.model.TransactionType;
import ru.medisov.home_finance.common.utils.ModelUtils;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.view.TransactionView;

import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionConverter {

    private final CategoryService categoryService;
    private final AccountService accountService;

    @Autowired
    public TransactionConverter(CategoryService categoryService, AccountService accountService) {
        this.categoryService = categoryService;
        this.accountService = accountService;
    }

    public TransactionView convert(TransactionModel transactionModel) {
        TransactionView transactionView = new TransactionView();
        transactionView
                .setId(transactionModel.getId())
                .setName(transactionModel.getName())
                .setAccount(transactionModel.getAccount().getName())
                .setAmount(transactionModel.getAmount())
                .setCategory(transactionModel.getCategory().getName())
                .setDateTime(formatDate(transactionModel.getDateTime().toLocalDate()))
//                .setTags(transactionModel.getTags().stream().map(TagModel::getName).collect(Collectors.toList()))
                .setTransactionType(transactionModel.getTransactionType().getName());

        return transactionView;
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
                //todo implement Tag Service
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

    private String formatDate(LocalDate date) {
        return date.toString();
    }
}
