package org.michibe.libraryshowcase.modules.user;

import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.springframework.lang.Nullable;

public class UserNotExistException extends IllegalStateException {
    @Nullable
    private final UserId userId;

    public UserNotExistException(@Nullable UserId userId) {
        super("User with id '" + userId + "' does not exist.");
        this.userId = userId;
    }
}
