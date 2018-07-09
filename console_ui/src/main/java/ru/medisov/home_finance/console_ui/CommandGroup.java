package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.console_ui.exception.HomeFinanceUIException;

import java.util.Optional;

public interface CommandGroup<T> {
    T save();

    T update();

    Optional<T> remove();

    T find() throws HomeFinanceUIException;
}
