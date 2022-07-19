package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.modules.user.model.Role;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record UpdateUserReqPayload(
        @NotEmpty
        @NonNull
        String name,
        @NotEmpty
        @NonNull
        String password,
        @NotNull
        @NonNull
        Role role
) {
}
