package ru.medisov.home_finance.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CurrencyModel {
    private long id;
    private String name;
    private String code;
    private String symbol;
}