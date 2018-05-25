package ru.medisov.home_finance.dao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionModel extends IdModel {
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private AccountModel account;
    private CategoryTransactionModel category;
    private TransactionType transactionType;
    private List<String> tags;

    public TransactionModel() {}

    public TransactionModel(BigDecimal amount, LocalDateTime dateTime, AccountModel account, CategoryTransactionModel category, TransactionType transactionType, List<String> tags) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.account = account;
        this.category = category;
        this.transactionType = transactionType;
        this.tags = tags;
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public CategoryTransactionModel getCategory() {
        return category;
    }

    public void setCategory(CategoryTransactionModel category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "id=" + getId() +
                ", account=" + account +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", transactionType=" + transactionType +
                ", category=" + category +
                ", tags=" + tags +
                '}';
    }
}
