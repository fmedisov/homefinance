package ru.medisov.home_finance.service.exception;

import org.springframework.stereotype.Component;

@Component
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