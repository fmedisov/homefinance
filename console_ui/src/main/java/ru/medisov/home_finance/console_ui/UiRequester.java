package ru.medisov.home_finance.console_ui;

public interface UiRequester<A, P> {

    A request(P parameter);
}
