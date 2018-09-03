package ru.medisov.home_finance.common.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.Amount;
import ru.medisov.home_finance.common.validator.DateSince;
import ru.medisov.home_finance.common.validator.Valid;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "transaction_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class TransactionModel implements SimpleModel<TransactionModel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Amount
    @Column(name = "amount")
    private BigDecimal amount;
    @DateSince(year = 1970, month = 1, day = 1, hour = 0, minute = 0)
    @Column(name = "datetime")
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "account")
    private AccountModel account;
    @ManyToOne
    @JoinColumn(name = "category")
    private CategoryTransactionModel category;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "tag_relation_tbl",
            joinColumns = {@JoinColumn(name = "transaction")},
            inverseJoinColumns = {@JoinColumn(name = "tag")}
    )
    private Set<TagModel> tags = new HashSet<>();
}
