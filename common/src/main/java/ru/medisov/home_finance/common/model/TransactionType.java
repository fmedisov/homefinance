package ru.medisov.home_finance.common.model;

import java.util.Arrays;

public enum  TransactionType {
    EXPENSE(1, "Expense"),
    INCOME(2, "Income");

    private final int number;
    private final String name;

    TransactionType(int number, String name) {
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
}
