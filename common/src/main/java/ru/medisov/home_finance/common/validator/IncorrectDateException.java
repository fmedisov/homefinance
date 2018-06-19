package ru.medisov.home_finance.common.validator;

public class IncorrectDateException extends Exception {
    public IncorrectDateException(String message, Throwable e) {
        super(message, e);
    }
}
