package ru.medisov.home_finance.common.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.medisov.home_finance.common.validator.Amount;
import ru.medisov.home_finance.common.validator.DateSince;
import ru.medisov.home_finance.common.validator.Valid;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaction_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name="TransactionModel.findAll",
                query="SELECT t FROM TransactionModel t"),
        @NamedQuery(name="TransactionModel.findByName",
                query="SELECT t FROM TransactionModel t WHERE t.name = :name"),
        @NamedQuery(name="TransactionModel.findByCategory",
                query="SELECT t FROM TransactionModel t WHERE t.category = :category"),
        @NamedQuery(name="TransactionModel.findByPeriod",
                query="SELECT t FROM TransactionModel t WHERE t.dateTime >= :dateFrom AND t.dateTime <= :upToDate"),
        @NamedQuery(name="TransactionModel.incomeByPeriod",
                query="SELECT t FROM TransactionModel t WHERE t.transactionType = 'Income'" +
                        " AND t.dateTime >= :dateFrom AND t.dateTime <= :upToDate"),
        @NamedQuery(name="TransactionModel.expenseByPeriod",
                query="SELECT t FROM TransactionModel t WHERE t.transactionType = 'Expense'" +
                        " AND t.dateTime >= :dateFrom AND t.dateTime <= :upToDate"),
})
public class TransactionModel extends TagModel {
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
    @Transient
    private List<TagModel> tags;
}
