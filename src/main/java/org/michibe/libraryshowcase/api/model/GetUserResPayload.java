package org.michibe.libraryshowcase.api.model;

import org.michibe.libraryshowcase.api.Paths;
import org.michibe.libraryshowcase.modules.user.model.Role;
import org.michibe.libraryshowcase.modules.user.model.User;
import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public record GetUserResPayload(
        @NonNull
        UserId id,
        @NonNull
        String name,
        @NonNull
        String email,
        @NonNull
        Role role,
        @NonNull
        Map<String, HalLink> _links
) {

    public static GetUserResPayload of(@NonNull User user) {
        final var links = new HashMap<String, HalLink>();
        links.put("self", HalLink.of(Paths.GET_USER.replace("{userId}", user.id().toString())));

        return new GetUserResPayload(
                user.id(),
                user.name(),
                user.email(),
                user.role(),
                links
        );
    }
}
