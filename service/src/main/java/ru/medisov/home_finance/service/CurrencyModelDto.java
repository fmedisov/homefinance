package ru.medisov.home_finance.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.dao.model.CurrencyModel;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CurrencyModelDto {
    private long id;
    private String name;
    private String code;
    private String symbol;

    public CurrencyModelDto(CurrencyModel currencyModel) {
        id = currencyModel.getId();
        name = currencyModel.getName();
        code = currencyModel.getCode();
        symbol = currencyModel.getSymbol();
    }
}
