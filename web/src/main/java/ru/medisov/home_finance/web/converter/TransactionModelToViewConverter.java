package ru.medisov.home_finance.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.web.view.TransactionView;

import java.time.LocalDate;

public class TransactionModelToViewConverter implements Converter<TransactionModel, TransactionView> {

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

    private String formatDate(LocalDate date) {
        return date.toString();
    }

}