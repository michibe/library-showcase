package org.michibe.libraryshowcase.modules.library;

import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.lang.Nullable;

public class CategoryNotExistException extends IllegalStateException {
    @Nullable
    private final CategoryId categoryId;

    public CategoryNotExistException(@Nullable CategoryId categoryId) {
        super("Category with id '" + categoryId + "' does not exist.");
        this.categoryId = categoryId;
    }
}
