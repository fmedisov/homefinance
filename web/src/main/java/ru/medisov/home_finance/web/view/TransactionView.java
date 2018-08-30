package ru.medisov.home_finance.web.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TransactionView {
    private Long id;
    private String name;
    private BigDecimal amount;
    private String dateTime;
    private String account;
    private String category;
    private String transactionType;
    private String tags;
}
