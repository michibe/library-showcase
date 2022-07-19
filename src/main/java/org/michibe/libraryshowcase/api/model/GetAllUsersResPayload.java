package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.api.Paths;
import org.michibe.libraryshowcase.modules.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GetAllUsersResPayload(
        Map<String, HalLink> _links,
        Map<String, Object> _embedded
) {

    public static GetAllUsersResPayload of(List<User> users) {
        final var links = new HashMap<String, HalLink>();
        links.put("self", new HalLink(Paths.GET_ALL_USERS));

        final var userResources = users.stream().map(GetUserResPayload::of);
        final var embedded = new HashMap<String, Object>();
        embedded.put("users", userResources);

        return new GetAllUsersResPayload(links, embedded);
    }

}
