package org.michibe.libraryshowcase.modules.library.persistence;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.michibe.libraryshowcase.modules.library.persistence.BookEntity.TABLE_NAME;

/**
 * I don't use auto generated ids because i prefer working with immutable types and with specific ids.
 */
@Entity
@Table(name = TABLE_NAME)
public class BookEntity {
    public static final String TABLE_NAME = "books";

    @Id
    @NotNull
    @NonNull
    private UUID id;

    @NotEmpty
    @NonNull
    private String title;

    @NotEmpty
    @NonNull
    private String author;

    @NotEmpty
    @NonNull
    private String publisher;

    @Min(0)
    @NonNull
    private int publishingYear;

    @Nullable
    private UUID categoryId;

    public BookEntity(
            @NonNull UUID id,
            @NonNull String title,
            @NonNull String author,
            @NonNull String publisher,
            @NonNull int publishingYear,
            @Nullable UUID categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        this.categoryId = categoryId;
    }

    public BookEntity() {
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    public String getPublisher() {
        return publisher;
    }

    @NonNull
    public int getPublishingYear() {
        return publishingYear;
    }

    @Nullable
    public UUID getCategoryId() {
        return categoryId;
    }
}
