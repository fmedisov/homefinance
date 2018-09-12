package ru.medisov.home_finance.service.social;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;
import ru.medisov.home_finance.common.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SocialUserDetailsImpl implements SocialUserDetails {

    private static final long serialVersionUID = -5246117266247684905L;

    private List<GrantedAuthority> list = new ArrayList<>();
    private UserModel userModel;

    public SocialUserDetailsImpl(UserModel userModel, List<String> roleNames) {
        this.userModel = userModel;

        for (String roleName : roleNames) {

            GrantedAuthority grant = new SimpleGrantedAuthority(roleName );
            this.list.add(grant);
        }
    }

    @Override
    public String getUserId() {
        return this.userModel.getUserId() + "";
    }

    @Override
    public String getUsername() {
        return userModel.getUserName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return list;
    }

    @Override
    public String getPassword() {
        return userModel.getEncrytedPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}