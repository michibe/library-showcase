package org.michibe.libraryshowcase.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.michibe.libraryshowcase.api.model.CreateCategoryReqPayload;
import org.michibe.libraryshowcase.api.model.GetAllCategoriesResPayload;
import org.michibe.libraryshowcase.api.model.GetCategoryResPayload;
import org.michibe.libraryshowcase.api.model.UpdateCategoryReqPayload;
import org.michibe.libraryshowcase.modules.library.CategoryNotExistException;
import org.michibe.libraryshowcase.modules.library.CategoryService;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "JWT")
record CategoryRestController(CategoryService categoryService) {

    @PostMapping(Paths.CREATE_CATEGORY)
    public ResponseEntity<GetCategoryResPayload> createCategory(
            @Valid @RequestBody CreateCategoryReqPayload reqPayload
    ) {
        final var createdCategory = categoryService.createCategory(reqPayload);
        return ResponseEntity.ok(GetCategoryResPayload.of(createdCategory));
    }

    @GetMapping(Paths.GET_ALL_CATEGORIES)
    public ResponseEntity<GetAllCategoriesResPayload> getAllCategories() {
        final var allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(GetAllCategoriesResPayload.of(allCategories));
    }

    @GetMapping(Paths.GET_CATEGORY)
    public ResponseEntity<GetCategoryResPayload> getCategory(
            @PathVariable("categoryId") CategoryId categoryId
    ) {
        final var category = categoryService.getCategory(categoryId);
        if (category.isPresent()) {
            return ResponseEntity.ok(GetCategoryResPayload.of(category.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(Paths.UPDATE_CATEGORY)
    public ResponseEntity<GetCategoryResPayload> updateCategory(
            @PathVariable("categoryId") CategoryId categoryId,
            @Valid @RequestBody UpdateCategoryReqPayload reqPayload
    ) {
        try {
            final var updatedCategory = categoryService.updateCategory(categoryId, reqPayload);
            return ResponseEntity.ok(GetCategoryResPayload.of(updatedCategory));
        } catch (CategoryNotExistException categoryNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(Paths.DELETE_CATEGORY)
    public ResponseEntity deleteCategory(
            @PathVariable("categoryId") CategoryId categoryId
    ) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok().build();
        } catch (CategoryNotExistException categoryNotExistException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
