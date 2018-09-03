package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "category_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class CategoryTransactionModel implements SimpleModel<CategoryTransactionModel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent")
    private CategoryTransactionModel parent;
}
