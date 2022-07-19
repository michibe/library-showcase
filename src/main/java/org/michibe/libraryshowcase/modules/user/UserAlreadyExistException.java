package org.michibe.libraryshowcase.modules.user;

import org.springframework.lang.NonNull;

public class UserAlreadyExistException extends IllegalStateException {
    @NonNull
    private final String email;

    public UserAlreadyExistException(String email) {
        super("User with email '" + email + "' already exist.");
        this.email = email;
    }
}
