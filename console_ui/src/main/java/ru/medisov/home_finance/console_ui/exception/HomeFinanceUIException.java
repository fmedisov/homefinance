package ru.medisov.home_finance.console_ui.exception;

public class HomeFinanceUIException extends RuntimeException {

    public HomeFinanceUIException() {
        super();
    }

    public HomeFinanceUIException(String message) {
        super(message);
    }

    public HomeFinanceUIException(String message, Throwable cause) {
        super(message, cause);
    }

    public HomeFinanceUIException(Throwable cause) {
        super(cause);
    }
}