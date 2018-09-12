package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.medisov.home_finance.common.model.UserModel;
import ru.medisov.home_finance.common.validator.ClassValidator;
import ru.medisov.home_finance.common.validator.Validator;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

public class CommonService {

    @Autowired
    private UserService userService;

    private Validator validator = new ClassValidator();

    public boolean validate(Object model) {
        if (!validator.isValid(model)) {
            throw new HomeFinanceServiceException("Модель " + model + " не валидирована");
        }

        return true;
    }

    public UserModel getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.findUserByUserName(userDetails.getUsername());
    }
}
