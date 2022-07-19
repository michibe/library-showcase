package org.michibe.libraryshowcase.modules.library.model;

import org.michibe.libraryshowcase.modules.library.persistence.CategoryEntity;
import org.springframework.lang.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

public record Category(
        @NonNull
        CategoryId id,
        @NonNull
        String title,
        @NonNull
        String description,
        @NonNull
        Set<Book> books
) {

    public static Category of(@NonNull CategoryEntity entity) {
        return new Category(
                CategoryId.ofUuid(entity.getId()),
                entity.getTitle(),
                entity.getDescription(),
                entity.getBooks().stream().map(Book::of).collect(Collectors.toSet())
        );
    }
}
