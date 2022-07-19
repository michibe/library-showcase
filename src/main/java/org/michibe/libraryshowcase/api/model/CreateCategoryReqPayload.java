package org.michibe.libraryshowcase.api.model;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;

public record CreateCategoryReqPayload(
        @NotEmpty
        @NonNull
        String title,
        @NotEmpty
        @NonNull
        String description
) {
}
