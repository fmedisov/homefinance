package ru.medisov.home_finance.service;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@Accessors(chain = true)
public class IncomeExpense {
    private BigDecimal income;
    private BigDecimal expense;
}
