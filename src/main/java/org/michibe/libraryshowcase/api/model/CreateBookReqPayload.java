package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateBookReqPayload(
        @NotEmpty
        @NonNull
        String title,
        @NotEmpty
        @NonNull
        String author,
        @NotEmpty
        @NonNull
        String publisher,

        @Min(0)
        @NotNull
        @NonNull
        int publishingYear,
        @Nullable
        CategoryId categoryId
) {
}
