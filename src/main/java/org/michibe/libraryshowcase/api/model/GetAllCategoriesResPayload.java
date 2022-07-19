package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.api.Paths;
import org.michibe.libraryshowcase.modules.library.model.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GetAllCategoriesResPayload(
        Map<String, HalLink> _links,
        Map<String, Object> _embedded
) {

    public static GetAllCategoriesResPayload of(List<Category> categories) {
        final var links = new HashMap<String, HalLink>();
        links.put("self", new HalLink(Paths.GET_ALL_CATEGORIES));

        final var categoryResources = categories.stream().map(GetCategoryResPayload::of);
        final var embedded = new HashMap<String, Object>();
        embedded.put("categories", categoryResources);

        return new GetAllCategoriesResPayload(links, embedded);
    }

}
