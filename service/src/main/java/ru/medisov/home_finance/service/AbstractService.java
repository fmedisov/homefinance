package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.validator.ClassValidator;
import ru.medisov.home_finance.common.validator.Validator;

public class AbstractService {

    private Validator validator = new ClassValidator();

    public boolean validate(Object model) {
        if (!validator.isValid(model)) {
            throw new HomeFinanceServiceException("Модель " + model + " не валидирована");
        }

        return true;
    }
}
