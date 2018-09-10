package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.Amount;
import ru.medisov.home_finance.common.validator.Valid;
import ru.medisov.home_finance.common.validator.NotEmpty;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "account_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class AccountModel implements SimpleModel<AccountModel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;
    @ManyToOne
    @JoinColumn(name = "currency")
    private CurrencyModel currencyModel;
    @Amount
    @Column(name = "amount")
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "user")
    private UserModel userModel;
}
