package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.RoleModel;
import ru.medisov.home_finance.common.model.UserModel;
import ru.medisov.home_finance.common.model.UserRoleModel;
import ru.medisov.home_finance.dao.repository.RoleRepository;
import ru.medisov.home_finance.dao.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<String> getRoleNames(Long userId) {
        List<UserRoleModel> userRoleModels = userRoleRepository.findAllByUserModel_UserId(userId);
        List<String> roleNames = new ArrayList<>();
        for (UserRoleModel userRoleModel : userRoleModels) {
            if (userRoleModel.getRoleModel() != null && userRoleModel.getRoleModel().getRoleName() != null) {
                roleNames.add(userRoleModel.getRoleModel().getRoleName());
            }
        }
        return roleNames;
    }

    @Override
    public RoleModel findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElse(null);
    }

    @Override
    public void createRoleFor(UserModel userModel, List<String> roleNames) {
        for (String roleName : roleNames) {
            RoleModel role = this.findRoleByName(roleName);

            if (role == null) {
                role = new RoleModel();
                role.setRoleName(RoleModel.ROLE_USER);
                roleRepository.save(role);
            }

            UserRoleModel userRoleModel = new UserRoleModel().setRoleModel(role).setUserModel(userModel);
            userRoleRepository.save(userRoleModel);
        }
    }
}
