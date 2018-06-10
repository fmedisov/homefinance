package ru.medisov.home_finance.service;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class IncomeExpense {
    private BigDecimal income;
    private BigDecimal expense;
}
