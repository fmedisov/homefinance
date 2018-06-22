package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class CategoryTransactionModel {
    private Long id;
    @NotEmpty
    private String name;
    private CategoryTransactionModel parent;
}
