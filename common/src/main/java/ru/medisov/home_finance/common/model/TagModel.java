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
public class TagModel {
    private Long id;
    @NotEmpty
    private String name;
    private long count;

    public TagModel(String name) {
        this.name = name;
    }

    public TagModel(TagModel model) {
        this.setId(model.getId());
        this.setName(model.getName());
        this.setCount(model.getCount());
    }
}