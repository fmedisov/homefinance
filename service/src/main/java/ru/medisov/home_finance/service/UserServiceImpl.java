package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.RoleModel;
import ru.medisov.home_finance.common.model.UserModel;
import ru.medisov.home_finance.dao.repository.UserRepository;
import ru.medisov.home_finance.service.utils.EncrytedPasswordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public UserModel findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public UserModel findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public String findAvailableUserName(String userName_prefix) {
        UserModel account = this.findUserByUserName(userName_prefix);
        if (account == null) {
            return userName_prefix;
        }
        int i = 0;
        while (true) {
            String userName = userName_prefix + "_" + i++;
            account = this.findUserByUserName(userName);
            if (account == null) {
                return userName;
            }
        }
    }

    @Override
    public UserModel createUser(Connection<?> connection) {
        ConnectionKey key = connection.getKey();

        System.out.println("key= (" + key.getProviderId() + "," + key.getProviderUserId() + ")");

        UserProfile userProfile = connection.fetchUserProfile();

        String email = userProfile.getEmail();
        UserModel userModel = this.findByEmail(email);
        if (userModel != null) {
            return userModel;
        }
        String userName_prefix = userProfile.getFirstName().trim().toLowerCase()//
                + "_" + userProfile.getLastName().trim().toLowerCase();

        String userName = this.findAvailableUserName(userName_prefix);

        String randomPassword = UUID.randomUUID().toString().substring(0, 5);
        String encrytedPassword = EncrytedPasswordUtils.encrytePassword(randomPassword);

        userModel = new UserModel().setEnabled(true)
                .setEncrytedPassword(encrytedPassword)
                .setUserName(userName)
                .setEmail(email)
                .setFirstName(userProfile.getFirstName())
                .setLastName(userProfile.getLastName());

        UserModel saved = userRepository.save(userModel);

        List<String> roleNames = new ArrayList<>();
        roleNames.add(RoleModel.ROLE_USER);
        roleService.createRoleFor(saved, roleNames);

        return saved;
    }

}