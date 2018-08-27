package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class CurrencyModel extends TagModel {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String code;
    @NotEmpty
    private String symbol;
}