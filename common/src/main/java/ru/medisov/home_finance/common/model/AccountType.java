package ru.medisov.home_finance.common.model;

import java.util.Arrays;
import java.util.Optional;

public enum AccountType {
    CASH(1, "Cash"),
    DEBIT_CARD(2, "Debit card"),
    CREDIT_CARD(3, "Credit card"),
    DEPOSIT(4, "Deposit");

    private final int number;
    private final String name;

    AccountType(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return number + " - " + name;
    }

    public static void print() {
        Arrays.stream(values()).forEach(System.out::println);
    }

    public static Optional<AccountType> findByNum(int num) {
        return Arrays.stream(AccountType.values()).filter(x -> x.number == num).findFirst();
    }
}
