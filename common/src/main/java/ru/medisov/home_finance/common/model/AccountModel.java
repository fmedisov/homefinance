package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.Amount;
import ru.medisov.home_finance.common.validator.Valid;
import ru.medisov.home_finance.common.validator.NotEmpty;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class AccountModel extends TagModel {
    private Long id;
    @NotEmpty
    private String name;
    private AccountType accountType;
    private CurrencyModel currencyModel;
    @Amount
    private BigDecimal amount;
}
