package org.michibe.libraryshowcase.modules.user.model;

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
public class UserId implements Serializable {
    @NonNull
    private final UUID value;

    private UserId(@NonNull UUID value) {
        Assert.notNull(value, "BookId value must not be null");
        this.value = value;
    }

    public static UserId createNew() {
        return new UserId(UUID.randomUUID());
    }

    @JsonCreator
    public static UserId ofString(String value) {
        return new UserId(UUID.fromString(value));
    }

    public static UserId ofUuid(UUID value) {
        return new UserId(value);
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
        UserId bookId = (UserId) o;
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