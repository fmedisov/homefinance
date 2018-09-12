package ru.medisov.home_finance.service;

import org.springframework.social.connect.Connection;
import ru.medisov.home_finance.common.model.UserModel;

public interface UserService {

    UserModel findUserByUserId(Long userId);

    UserModel findUserByUserName(String userName);

    UserModel findByEmail(String email);

    String findAvailableUserName(String userName_prefix);

    UserModel createUser(Connection<?> connection);
}