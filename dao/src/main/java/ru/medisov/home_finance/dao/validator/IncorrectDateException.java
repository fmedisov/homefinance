package ru.medisov.home_finance.dao.validator;

public class IncorrectDateException extends Exception {
    public IncorrectDateException(String message, Throwable e) {
        super(message, e);
    }
}
