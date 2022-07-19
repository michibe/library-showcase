package org.michibe.libraryshowcase.modules.library.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Collections;

@DataJpaTest()
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository cut;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void cleanup() {
        cut.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void saveAndGet() {
        final var category = new CategoryEntity(
                CategoryId.createNew().getValue(),
                "Adventure",
                "Books full of adventure",
                Collections.emptyList()
        );

        final var result = this.cut.save(category);

        Assertions.assertThat(result.getId()).isEqualTo(category.getId());
        Assertions.assertThat(result.getTitle()).isEqualTo(category.getTitle());
        Assertions.assertThat(result.getDescription()).isEqualTo(category.getDescription());
        Assertions.assertThat(result.getBooks()).isEmpty();
    }
}