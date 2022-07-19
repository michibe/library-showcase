package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.modules.user.model.Role;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateUserReqPayload(
        @NotEmpty
        @NonNull
        String name,
        @NotEmpty
        @Email
        @NonNull
        String email,
        @NotEmpty
        @NonNull
        String password,
        @NotNull
        @NonNull
        Role role
) {
}
