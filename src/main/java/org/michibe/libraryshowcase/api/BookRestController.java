package org.michibe.libraryshowcase.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.michibe.libraryshowcase.api.model.CreateBookReqPayload;
import org.michibe.libraryshowcase.api.model.GetAllBooksResPayload;
import org.michibe.libraryshowcase.api.model.GetBookResPayload;
import org.michibe.libraryshowcase.api.model.UpdateBookReqPayload;
import org.michibe.libraryshowcase.modules.library.BookNotExistException;
import org.michibe.libraryshowcase.modules.library.BookService;
import org.michibe.libraryshowcase.modules.library.CategoryNotExistException;
import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "JWT")
record BookRestController(BookService bookService) {

    @PostMapping(Paths.CREATE_BOOK)
    public ResponseEntity<GetBookResPayload> createBook(
            @Valid @RequestBody CreateBookReqPayload reqPayload
    ) {
        try {
            final var createdBook = bookService.createBook(reqPayload);
            return ResponseEntity.ok(GetBookResPayload.of(createdBook));
        } catch (CategoryNotExistException categoryNotExistException) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).build();
        }
    }

    @GetMapping(Paths.GET_ALL_BOOKS)
    public ResponseEntity<GetAllBooksResPayload> getAllBooks() {
        final var allBooks = bookService.getAllBooks();
        return ResponseEntity.ok(GetAllBooksResPayload.of(allBooks));
    }

    @GetMapping(Paths.GET_BOOK)
    public ResponseEntity<GetBookResPayload> getBook(
            @PathVariable("bookId") BookId bookId
    ) {
        final var book = bookService.getBook(bookId);
        if (book.isPresent()) {
            return ResponseEntity.ok(GetBookResPayload.of(book.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(Paths.UPDATE_BOOK)
    public ResponseEntity<GetBookResPayload> updateBook(
            @PathVariable("bookId") BookId bookId,
            @Valid @RequestBody UpdateBookReqPayload reqPayload
    ) {
        try {
            final var updatedBook = bookService.updateBook(bookId, reqPayload);
            return ResponseEntity.ok(GetBookResPayload.of(updatedBook));
        } catch (BookNotExistException bookNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CategoryNotExistException categoryNotExistException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(Paths.DELETE_BOOK)
    public ResponseEntity deleteBook(
            @PathVariable("bookId") BookId bookId
    ) {
        try {
            bookService.deleteBook(bookId);
            return ResponseEntity.ok().build();
        } catch (BookNotExistException bookNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
