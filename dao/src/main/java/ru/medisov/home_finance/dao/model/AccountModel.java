package ru.medisov.home_finance.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountModel {
    private long id;
    private String name;
    private AccountType accountType;
    private CurrencyModel currencyModel;
    private BigDecimal amount;
}
