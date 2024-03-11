package com.capstone.BnagFer.domain.accounts.jwt.userdetails;

import com.capstone.BnagFer.domain.accounts.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final Boolean isStaff;

    public CustomUserDetails(User user) {
        email = user.getEmail();
        password = user.getPassword();
        isStaff = user.getIsStaff();
    }

    public CustomUserDetails(String email, String password, Boolean isStaff) {
        this.email = email;
        this.password = password;
        this.isStaff = isStaff;
    }

    public Boolean getStaff() {
        return isStaff;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
