package ru.medisov.home_finance.dao.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class TransactionModel {
    private long id;
    private String name;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private AccountModel account;
    private CategoryTransactionModel category;
    private TransactionType transactionType;
    private List<String> tags;
}
