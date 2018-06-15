package ru.medisov.home_finance.service;

public class HomeFinanceServiceException extends RuntimeException {

    public HomeFinanceServiceException() {
        super();
    }

    public HomeFinanceServiceException(String message) {
        super(message);
    }

    public HomeFinanceServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public HomeFinanceServiceException(Throwable cause) {
        super(cause);
    }
}