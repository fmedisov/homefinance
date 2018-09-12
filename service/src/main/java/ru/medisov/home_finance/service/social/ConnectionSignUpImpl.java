package ru.medisov.home_finance.service.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.UserModel;
import ru.medisov.home_finance.service.UserService;

@Component
@Service
public class ConnectionSignUpImpl implements ConnectionSignUp {

    private UserService userService;

    @Autowired
    public ConnectionSignUpImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(Connection<?> connection) {
        UserModel account = userService.createUser(connection);
        return account.getUserName();
    }
}