package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.model.RoleModel;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.List;

public interface RoleService {
    List<String> getRoleNames(Long userId);

    RoleModel findRoleByName(String roleName);

    void createRoleFor(UserModel userModel, List<String> roleNames);
}
