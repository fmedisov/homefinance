package ru.medisov.home_finance.dao.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.dao.validator.DateSince;
import ru.medisov.home_finance.dao.validator.NotEmpty;
import ru.medisov.home_finance.dao.validator.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@Valid
public class TransactionModel {
    private long id;
    @NotEmpty
    private String name;
    private BigDecimal amount;
    @DateSince(year = 1970, month = 1, day = 1)
    private LocalDateTime dateTime;
    private AccountModel account;
    private CategoryTransactionModel category;
    private TransactionType transactionType;
    private List<TagModel> tags;
}
