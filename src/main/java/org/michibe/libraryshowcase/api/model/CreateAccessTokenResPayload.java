package org.michibe.libraryshowcase.api.model;

import org.springframework.lang.NonNull;

public record CreateAccessTokenResPayload(
        @NonNull
        String jwt
) {

}
