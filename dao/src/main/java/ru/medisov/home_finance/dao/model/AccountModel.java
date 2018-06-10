package ru.medisov.home_finance.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.dao.validator.NotEmpty;
import ru.medisov.home_finance.dao.validator.Valid;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Valid
public class AccountModel {
    private long id;
    @NotEmpty
    private String name;
    private AccountType accountType;
    private CurrencyModel currencyModel;
    private BigDecimal amount;
}
