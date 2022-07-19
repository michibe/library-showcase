package org.michibe.libraryshowcase.modules.library.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository cut;

    @BeforeEach
    public void cleanup() {
        cut.deleteAll();
    }

    @Test
    public void saveAndGet() {
        final var book = new BookEntity(
                BookId.createNew().getValue(),
                "Harry Potter and the Philosopher's Stone",
                "Joanne K. Rowling",
                "Bloomsbury Publishing",
                1997,
                null
        );

        final var createdBook = this.cut.save(book);

        Assertions.assertThat(createdBook.getId()).isEqualTo(book.getId());
        Assertions.assertThat(createdBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(createdBook.getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(createdBook.getPublisher()).isEqualTo(book.getPublisher());
        Assertions.assertThat(createdBook.getPublishingYear()).isEqualTo(book.getPublishingYear());
        Assertions.assertThat(createdBook.getCategoryId()).isNull();
    }
}