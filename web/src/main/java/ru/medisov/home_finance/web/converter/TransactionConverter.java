package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.*;
import ru.medisov.home_finance.common.utils.ModelUtils;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.TagService;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;
import ru.medisov.home_finance.web.view.TransactionView;

import java.time.LocalDate;

@Component
public class TransactionConverter {

    private final CategoryService categoryService;
    private final AccountService accountService;
    private final TagService tagService;

    @Autowired
    public TransactionConverter(CategoryService categoryService, AccountService accountService, TagService tagService) {
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.tagService = tagService;
    }

    public TransactionView toTransactionView(TransactionModel transactionModel) {
        TransactionView transactionView = new TransactionView();
        transactionView
                .setId(transactionModel.getId())
                .setName(transactionModel.getName())
                .setAccount(transactionModel.getAccount().getName())
                .setAmount(transactionModel.getAmount())
                .setCategory(transactionModel.getCategory().getName())
                .setDateTime(formatDate(transactionModel.getDateTime().toLocalDate()))
                .setTags(getTagsString(transactionModel))
                .setTransactionType(transactionModel.getTransactionType().getName());

        return transactionView;
    }

    public TransactionModel toTransactionModel(TransactionView transactionView) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel
                .setId(transactionView.getId())
                .setName(transactionView.getName())
                .setAmount(transactionView.getAmount())
                .setDateTime(ModelUtils.parseDateTime(transactionView.getDateTime()))
                .setAccount(getAccount(transactionView.getAccount()))
                .setCategory(getCategory(transactionView.getCategory()))
                .setTransactionType(TransactionType.findByName(transactionView.getTransactionType()).orElse(null))
                .setTags(tagService.fromStringList(transactionView.getTags(), " "));

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

    private String getTagsString(TransactionModel model) {
        StringBuilder builder = new StringBuilder();

        if (model.getTags() != null && model.getTags().size() > 0) {
            model.getTags().forEach(t -> {
                if (t != null) {
                    builder.append(t.getName()).append(" ");
                }
            });
        }

        return builder.toString();
    }
}
