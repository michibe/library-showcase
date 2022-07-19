package org.michibe.libraryshowcase.api.model;

import org.springframework.lang.NonNull;

public record HalLink(@NonNull String href) {
    public static HalLink of(@NonNull String href) {
        return new HalLink(href);
    }
}
