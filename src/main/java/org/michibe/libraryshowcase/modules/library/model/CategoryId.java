package org.michibe.libraryshowcase.modules.library.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CategoryId implements Serializable {
    @NonNull
    private final UUID value;

    private CategoryId(@NonNull UUID value) {
        Assert.notNull(value, "CategoryId value must not be null");
        this.value = value;
    }
    @NonNull
    public static CategoryId createNew() {
        return new CategoryId(UUID.randomUUID());
    }

    @JsonCreator
    @NonNull
    public static CategoryId ofString(@NonNull String value) {
        return new CategoryId(UUID.fromString(value));
    }

    @NonNull
    public static CategoryId ofUuid(@NonNull UUID value) {
        return new CategoryId(value);
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
        CategoryId that = (CategoryId) o;
        return value.equals(that.value);
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
