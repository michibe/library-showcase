package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.api.Paths;
import org.michibe.libraryshowcase.modules.library.model.Book;
import org.michibe.libraryshowcase.modules.library.model.Category;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public record GetCategoryResPayload(
        @NonNull
        CategoryId id,
        @NonNull
        String title,
        @NonNull
        String description,
        @NonNull
        int numberOfAssignedBooks,
        @NonNull
        Map<String, HalLink> _links,
        @NonNull
        Map<String, Object> _embedded
) {

    public static GetCategoryResPayload of(@NonNull Category category) {
        final var links = new HashMap<String, HalLink>();
        links.put("self", HalLink.of(Paths.GET_CATEGORY.replace("{categoryId}", category.id().toString())));

        final var embeddedBooks = category.books().stream().map(EmbeddedBook::of);
        final var embedded = new HashMap<String, Object>();
        embedded.put("assignedBooks", embeddedBooks);

        return new GetCategoryResPayload(
                category.id(),
                category.title(),
                category.description(),
                category.books().size(),
                links,
                embedded
        );
    }

    public record EmbeddedBook(
            @NonNull
            String title,
            @NonNull
            Map<String, HalLink> _links
    ) {
        public static EmbeddedBook of(Book book) {
            final var links = new HashMap<String, HalLink>();
            links.put("self", HalLink.of(Paths.GET_BOOK.replace("{bookId}", book.id().toString())));

            return new EmbeddedBook(
                    book.title(),
                    links
            );
        }
    }

}
