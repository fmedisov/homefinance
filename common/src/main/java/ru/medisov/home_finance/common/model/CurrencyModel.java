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
@Table(name = "currency_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class CurrencyModel implements SimpleModel<CurrencyModel> {
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
    @ManyToOne
    @JoinColumn(name = "user")
    private UserModel userModel;
}