package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.Valid;
import ru.medisov.home_finance.common.validator.NotEmpty;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class AccountModel {
    private Long id;
    @NotEmpty
    private String name;
    private AccountType accountType;
    private CurrencyModel currencyModel;
    private BigDecimal amount;
}
