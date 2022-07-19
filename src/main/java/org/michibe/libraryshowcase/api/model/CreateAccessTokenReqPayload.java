package org.michibe.libraryshowcase.api.model;

import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public record CreateAccessTokenReqPayload(
        @NotEmpty
        @Email
        @NonNull
        String email,
        @NotEmpty
        @NonNull
        String password
) {
}
