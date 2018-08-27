package ru.medisov.home_finance.web.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CurrencyView {
    private Long id;
    private String name;
    private String code;
    private String symbol;
}