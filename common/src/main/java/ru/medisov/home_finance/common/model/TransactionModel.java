package ru.medisov.home_finance.common.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.DateSince;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class TransactionModel {
    private long id;
    @NotEmpty
    private String name;
    private BigDecimal amount;
    @DateSince(year = 1970, month = 1, day = 1, hour = 0, minute = 0)
    private LocalDateTime dateTime;
    private AccountModel account;
    private CategoryTransactionModel category;
    private TransactionType transactionType;
    private List<TagModel> tags;
}
