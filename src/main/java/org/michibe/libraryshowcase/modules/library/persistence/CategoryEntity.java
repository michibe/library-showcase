package org.michibe.libraryshowcase.modules.library.persistence;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.michibe.libraryshowcase.modules.library.persistence.CategoryEntity.TABLE_NAME;


@Entity
@Table(name = TABLE_NAME)
public class CategoryEntity {

    public static final String TABLE_NAME = "categories";

    @Id
    @NonNull
    private UUID id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "categoryId")
    private List<BookEntity> books = Collections.emptyList();

    public CategoryEntity() {
        super();
    }

    public CategoryEntity(
            @NonNull UUID id,
            @NonNull String title,
            @NonNull String description,
            @NonNull List<BookEntity> books
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.books = books;
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
    public String getDescription() {
        return description;
    }

    @NonNull
    public List<BookEntity> getBooks() {
        return books;
    }

}
