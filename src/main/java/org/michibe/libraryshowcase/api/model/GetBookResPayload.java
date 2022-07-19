package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.api.Paths;
import org.michibe.libraryshowcase.modules.library.model.Book;
import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public record GetBookResPayload(
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
        @NonNull
        Map<String, HalLink> _links
) {

    public static GetBookResPayload of(@NonNull Book book) {
        final var links = new HashMap<String, HalLink>();
        links.put("self", HalLink.of(Paths.GET_BOOK.replace("{bookId}", book.id().toString())));
        if (book.categoryId() != null) {
            links.put("category", HalLink.of(Paths.GET_CATEGORY.replace("{categoryId}", book.categoryId().toString())));
        }

        return new GetBookResPayload(
                book.id(),
                book.title(),
                book.author(),
                book.publisher(),
                book.publishingYear(),
                links
        );
    }
}
