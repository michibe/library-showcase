package org.michibe.libraryshowcase.modules.library.model;

import org.michibe.libraryshowcase.modules.library.persistence.BookEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record Book(
        @NonNull
        BookId id,
        @NonNull
        String title,
        @NonNull
        String author,
        @NonNull
        String publisher,
        @NonNull
        int publishingYear,
        @Nullable
        CategoryId categoryId
) {

    public static Book of(@NonNull BookEntity entity) {
        final var categoryId = (entity.getCategoryId() != null) ? CategoryId.ofUuid(entity.getCategoryId()) : null;
        return new Book(
                BookId.ofUuid(entity.getId()),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getPublisher(),
                entity.getPublishingYear(),
                categoryId
        );
    }
}
