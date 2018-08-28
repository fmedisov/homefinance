package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

import javax.persistence.*;

@Entity
@Table(name = "category_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class CategoryTransactionModel extends TagModel{
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
