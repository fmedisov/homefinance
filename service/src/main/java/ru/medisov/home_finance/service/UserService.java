package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.UserModel;

import java.util.Optional;

public interface UserService {

    Optional<UserModel> getUser(String login);

}