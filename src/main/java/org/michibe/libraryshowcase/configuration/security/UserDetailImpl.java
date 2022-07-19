package org.michibe.libraryshowcase.configuration.security;

import org.michibe.libraryshowcase.modules.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

class UserDetailImpl implements UserDetails {

    private final String email;
    private final String password;
    private final SimpleGrantedAuthority authority;

    public UserDetailImpl(String email, String password, SimpleGrantedAuthority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public static UserDetailImpl of(User user) {
        return new UserDetailImpl(
                user.email(),
                user.encryptedPassword(),
                new SimpleGrantedAuthority(user.role().name())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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