package ru.medisov.home_finance.web.exception;

public class HomeFinanceWebException extends RuntimeException {

    public HomeFinanceWebException() {
        super();
    }

    public HomeFinanceWebException(String message) {
        super(message);
    }

    public HomeFinanceWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public HomeFinanceWebException(Throwable cause) {
        super(cause);
    }
}