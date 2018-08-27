package ru.medisov.home_finance.common.model;

import java.util.Arrays;
import java.util.Optional;

public enum  TransactionType {
    EXPENSE(1, "Расход"),
    INCOME(2, "Доход");

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

    public static Optional<TransactionType> findByNum(int num) {
        return Arrays.stream(TransactionType.values()).filter(x -> x.number == num).findFirst();
    }

    public static Optional<TransactionType> findByName(String name) {
        return Arrays.stream(TransactionType.values()).filter(x -> x.name.equals(name)).findFirst();
    }
}
