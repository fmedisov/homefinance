package ru.medisov.home_finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.medisov.home_finance.common.model.UserModel;
import ru.medisov.home_finance.common.model.UserRoleEnum;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.HashSet;
import java.util.Set;

@Component
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        try {
            UserModel user = userService.getUser(name).orElseThrow(HomeFinanceServiceException::new);

            Set<GrantedAuthority> roles = new HashSet<>();
            roles.add(new SimpleGrantedAuthority(UserRoleEnum.USER.name()));

            return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), roles);
        } catch (HomeFinanceServiceException e) {
            e.printStackTrace();
            return null;
        }
    }
}