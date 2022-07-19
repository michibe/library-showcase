package org.michibe.libraryshowcase.modules.library.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * I prefer working with types as much as possible and most important for ids. This prevents mixing up different ids, if they are all integers or longs.
 */
public class BookId implements Serializable {
    @NonNull
    private final UUID value;

    private BookId(@NonNull UUID value) {
        Assert.notNull(value, "BookId value must not be null");
        this.value = value;
    }

    @NonNull
    public static BookId createNew() {
        return new BookId(UUID.randomUUID());
    }

    @JsonCreator
    @NonNull
    public static BookId ofString(@NonNull String value) {
        return new BookId(UUID.fromString(value));
    }

    @NonNull
    public static BookId ofUuid(@NonNull UUID value) {
        return new BookId(value);
    }

    @NonNull
    @JsonValue
    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookId bookId = (BookId) o;
        return value.equals(bookId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}