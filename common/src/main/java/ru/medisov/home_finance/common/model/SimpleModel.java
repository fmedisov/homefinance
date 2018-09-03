package ru.medisov.home_finance.common.model;

public interface SimpleModel<T> {

    T setId(Long Id);

    Long getId();

    T setName(String name);

    String getName();
}
