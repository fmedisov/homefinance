package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

import javax.persistence.*;

@Entity
@Table(name = "currency_tbl")
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name="CurrencyModel.findAll",
                query="SELECT c FROM CurrencyModel c"),
        @NamedQuery(name="CurrencyModel.findByName",
                query="SELECT c FROM CurrencyModel c WHERE c.name = :name"),
})
public class CurrencyModel extends TagModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @NotEmpty
    //todo что делать, если введено слишком длинное значение
    @Column(name = "code")
    private String code;
    @NotEmpty
    @Column(name = "symbol")
    private String symbol;
}