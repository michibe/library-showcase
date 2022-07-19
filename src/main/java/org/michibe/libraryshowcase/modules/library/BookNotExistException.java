package org.michibe.libraryshowcase.modules.library;

import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.springframework.lang.Nullable;

public class BookNotExistException extends IllegalStateException {
    @Nullable
    private final BookId bookId;

    public BookNotExistException(BookId bookId) {
        super("Book with id '" + bookId + "' does not exist.");
        this.bookId = bookId;
    }
}
