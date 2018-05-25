package ru.medisov.home_finance.dao.model;

import java.math.BigDecimal;

public class AccountModel extends IdModel {
    private String name;
    private AccountType accountType;
    private CurrencyModel currencyModel;
    private BigDecimal amount;

    public AccountModel() {}

    public AccountModel(String name, AccountType accountType, CurrencyModel currencyModel, BigDecimal amount) {
        this.currencyModel = currencyModel;
        this.name = name;
        this.accountType = accountType;
        this.amount = amount;
    }

    public AccountModel(long id, String name, AccountType accountType, CurrencyModel currencyModel, BigDecimal amount) {
        this(name, accountType, currencyModel, amount);
        super.setId(id);
    }

    public CurrencyModel getCurrencyModel() {
        return currencyModel;
    }

    public void setCurrencyModel(CurrencyModel currencyModel) {
        this.currencyModel = currencyModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "id=" + getId() +
                ", currencyModel=" + currencyModel +
                ", name='" + name + '\'' +
                ", accountType=" + accountType +
                ", amount=" + amount +
                '}';
    }
}
