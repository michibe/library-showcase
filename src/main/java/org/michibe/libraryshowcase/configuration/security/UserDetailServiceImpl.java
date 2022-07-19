package org.michibe.libraryshowcase.configuration.security;

import org.michibe.libraryshowcase.modules.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
record UserDetailServiceImpl(
        UserService userService
) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService
                .getUserByEmail(username)
                .map(UserDetailImpl::of)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '" + username + "' not found."));
    }
}
