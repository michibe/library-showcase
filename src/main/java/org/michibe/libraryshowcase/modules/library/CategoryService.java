package org.michibe.libraryshowcase.modules.library;

import org.michibe.libraryshowcase.api.model.CreateCategoryReqPayload;
import org.michibe.libraryshowcase.api.model.UpdateCategoryReqPayload;
import org.michibe.libraryshowcase.modules.library.model.Category;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.michibe.libraryshowcase.modules.library.persistence.CategoryEntity;
import org.michibe.libraryshowcase.modules.library.persistence.CategoryRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record CategoryService(
        CategoryRepository categoryRepository
) {
    public List<Category> getAllCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(Category::of)
                .collect(Collectors.toList());
    }

    public Optional<Category> getCategory(@NonNull CategoryId categoryId) {
        return categoryRepository.
                findById(categoryId.getValue())
                .map(Category::of);
    }

    public Category createCategory(@NonNull CreateCategoryReqPayload reqPayload) {
        final var createdCategory = createOrUpdateCategory(new CategoryEntity(
                CategoryId.createNew().getValue(),
                reqPayload.title(),
                reqPayload.description(),
                Collections.emptyList()
        ));
        return Category.of(createdCategory);
    }


    public Category updateCategory(
            @NonNull CategoryId id,
            @NonNull UpdateCategoryReqPayload reqPayload
    ) {

        final var category = categoryRepository.findById(id.getValue());

        if (category.isEmpty()) {
            throw new CategoryNotExistException(id);
        }

        final var updatedCategory = createOrUpdateCategory(new CategoryEntity(
                category.get().getId(),
                reqPayload.title(),
                reqPayload.description(),
                category.get().getBooks()
        ));

        return Category.of(updatedCategory);
    }

    public void deleteCategory(CategoryId id) {
        final var category = categoryRepository.findById(id.getValue());
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
        } else {
            throw new CategoryNotExistException(id);
        }
    }


    private CategoryEntity createOrUpdateCategory(CategoryEntity entity) {
        return categoryRepository.save(entity);
    }
}
