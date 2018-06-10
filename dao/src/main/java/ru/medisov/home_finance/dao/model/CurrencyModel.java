package ru.medisov.home_finance.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.dao.validator.NotEmpty;
import ru.medisov.home_finance.dao.validator.Valid;

@Data
@Accessors(chain = true)
@Valid
public class CurrencyModel {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String code;
    @NotEmpty
    private String symbol;
}