package ru.medisov.home_finance.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.dao.validator.NotEmpty;
import ru.medisov.home_finance.dao.validator.Valid;

@Data
@Accessors(chain = true)
@Valid
public class CategoryTransactionModel {
    private long id;
    @NotEmpty
    private String name;
    private CategoryTransactionModel parent;
}
