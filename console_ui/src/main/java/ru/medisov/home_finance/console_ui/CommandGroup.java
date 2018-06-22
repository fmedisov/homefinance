package ru.medisov.home_finance.console_ui;

import java.util.Optional;

public interface CommandGroup<T> {
    T save();

    T update();

    Optional<T> remove();

    T find();
}
