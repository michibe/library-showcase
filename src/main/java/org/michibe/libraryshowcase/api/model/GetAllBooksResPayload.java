package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.api.Paths;
import org.michibe.libraryshowcase.modules.library.model.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GetAllBooksResPayload(
        Map<String, HalLink> _links,
        Map<String, Object> _embedded
) {

    public static GetAllBooksResPayload of(List<Book> books) {
        final var links = new HashMap<String, HalLink>();
        links.put("self", new HalLink(Paths.GET_ALL_BOOKS));

        final var bookResources = books.stream().map(GetBookResPayload::of);
        final var embedded = new HashMap<String, Object>();
        embedded.put("books", bookResources);

        return new GetAllBooksResPayload(links, embedded);

    }

}
