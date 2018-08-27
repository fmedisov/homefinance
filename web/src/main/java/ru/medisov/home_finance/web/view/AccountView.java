package ru.medisov.home_finance.web.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AccountView {
    private Long id;
    private String name;
    private String accountType;
    private String currencyModel;
    private BigDecimal amount;
    private Boolean removed;

    @Override
    public String toString() {
        return name;
    }
}
