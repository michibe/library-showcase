package org.michibe.libraryshowcase.modules.library;

import org.hibernate.exception.ConstraintViolationException;
import org.michibe.libraryshowcase.api.model.CreateBookReqPayload;
import org.michibe.libraryshowcase.api.model.UpdateBookReqPayload;
import org.michibe.libraryshowcase.modules.library.model.Book;
import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.michibe.libraryshowcase.modules.library.persistence.BookEntity;
import org.michibe.libraryshowcase.modules.library.persistence.BookRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record BookService(
        BookRepository bookRepository
) {
    public List<Book> getAllBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(Book::of)
                .collect(Collectors.toList());
    }

    public Optional<Book> getBook(@NonNull BookId bookId) {
        return bookRepository.
                findById(bookId.getValue())
                .map(Book::of);
    }

    public Book createBook(@NonNull CreateBookReqPayload createBookReqPayload) {
        final var categoryId = (createBookReqPayload.categoryId() != null) ? createBookReqPayload.categoryId().getValue() : null;

        final var createdBook = createOrUpdateBook(new BookEntity(
                BookId.createNew().getValue(),
                createBookReqPayload.title(),
                createBookReqPayload.author(),
                createBookReqPayload.publisher(),
                createBookReqPayload.publishingYear(),
                categoryId
        ));
        return Book.of(createdBook);
    }


    public Book updateBook(
            @NonNull BookId id,
            @NonNull UpdateBookReqPayload updateBookData
    ) {

        final var book = bookRepository.findById(id.getValue());

        if (book.isEmpty()) {
            throw new BookNotExistException(id);
        }

        final var updatedBook = createOrUpdateBook(new BookEntity(
                book.get().getId(),
                updateBookData.title(),
                updateBookData.author(),
                updateBookData.publisher(),
                updateBookData.publishingYear(),
                (updateBookData.categoryId() != null) ? updateBookData.categoryId().getValue() : null
        ));


        return Book.of(updatedBook);
    }

    public void deleteBook(@NonNull BookId id) {
        final var book = this.bookRepository.findById(id.getValue());
        if (book.isPresent()) {
            this.bookRepository.delete(book.get());
        } else {
            throw new BookNotExistException(id);
        }
    }


    private BookEntity createOrUpdateBook(@NonNull BookEntity bookEntity) {
        try {
            return bookRepository.save(bookEntity);
        } catch (DataIntegrityViolationException e) {
            final var cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                if (((ConstraintViolationException) cause).getSQLException().getMessage().contains("FK__BOOKS__CATEGORY_ID")) {
                    final var categoryId = bookEntity.getCategoryId();
                    throw new CategoryNotExistException((categoryId != null) ? CategoryId.ofUuid(categoryId) : null);
                }
            }
            throw e;
        }
    }

}
