package org.michibe.libraryshowcase.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CategoryRestControllerIntTest extends BaseIntTest {

    private static BookId BOOK_ID_1;
    private static CategoryId CATEGORY_ID_1;

    @Test
    @Order(1)
    public void postCategory_return200WithCreatedCategory() {
        final var response = RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Adventure\",\n" +
                        "  \"description\": \"Very interesting\"\n" +
                        "}")
                .post(Paths.CREATE_CATEGORY)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var categoryId = response.getString("id");
        assertThat(categoryId).isNotEmpty();
        assertThat(response.getString("title")).isEqualTo("Adventure");
        assertThat(response.getString("description")).isEqualTo("Very interesting");
        assertThat(response.getInt("numberOfAssignedBooks")).isZero();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_CATEGORY.replace("{categoryId}", categoryId));

        CATEGORY_ID_1 = CategoryId.ofString(categoryId);
    }


    @Test
    @Order(2)
    public void postCategory_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{}")
                .post(Paths.CREATE_CATEGORY)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(3)
    public void getCategoryWithBook_return200WithRequestedCategory() {
        final var createdBookId = createBookForCategory(CATEGORY_ID_1);

        final var response = RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("categoryId", CATEGORY_ID_1.toString())
                .get(Paths.GET_CATEGORY)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var id = response.getString("id");
        assertThat(id).isEqualTo(CATEGORY_ID_1.toString());
        assertThat(response.getString("title")).isEqualTo("Adventure");
        assertThat(response.getString("description")).isEqualTo("Very interesting");
        assertThat(response.getInt("numberOfAssignedBooks")).isOne();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_CATEGORY.replace("{categoryId}", id));
        assertThat(response.getString("_embedded.assignedBooks[0].title")).isEqualTo("Harry Potter");
        assertThat(response.getString("_embedded.assignedBooks[0]._links.self.href")).isEqualTo(Paths.GET_BOOK.replace("{bookId}", createdBookId));

        BOOK_ID_1 = BookId.ofString(createdBookId);
    }

    @Test
    @Order(4)
    public void getCategory_categoryNotExist_return404() {
        RestAssured.given()
                .pathParam("categoryId", UUID.randomUUID().toString())
                .accept(ContentType.JSON)
                .get(Paths.GET_CATEGORY)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(5)
    public void getCategory_invalidCategoryId_return400() {
        RestAssured.given()
                .pathParam("categoryId", "12344234")
                .accept(ContentType.JSON)
                .get(Paths.GET_CATEGORY)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(6)
    public void getAllCategories_return200WithRequestedCategories() {
        final var response = RestAssured.given()
                .accept(ContentType.JSON)
                .get(Paths.GET_ALL_CATEGORIES)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        assertThat(response.getList("_embedded.categories")).hasSize(1);
        assertThat(response.getString("_embedded.categories[0].title")).isEqualTo("Adventure");
    }

    @Test
    @Order(7)
    public void updateCategory_return200WithUpdatedCategory() {
        final var response = RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("categoryId", CATEGORY_ID_1.toString())
                .body("{\n" +
                        "  \"title\": \"Bad Adventure\",\n" +
                        "  \"description\": \"Not interesting\"\n" +
                        "}")
                .put(Paths.UPDATE_CATEGORY)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var id = response.getString("id");
        assertThat(id).isEqualTo(CATEGORY_ID_1.toString());
        assertThat(response.getString("title")).isEqualTo("Bad Adventure");
        assertThat(response.getString("description")).isEqualTo("Not interesting");
        assertThat(response.getInt("numberOfAssignedBooks")).isOne();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_CATEGORY.replace("{categoryId}", id));
        assertThat(response.getString("_embedded.assignedBooks[0].title")).isEqualTo("Harry Potter");
        assertThat(response.getString("_embedded.assignedBooks[0]._links.self.href")).isEqualTo(Paths.GET_BOOK.replace("{bookId}", BOOK_ID_1.toString()));
    }


    @Test
    @Order(8)
    public void updateCategory_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("categoryId", CATEGORY_ID_1.toString())
                .post(Paths.UPDATE_CATEGORY)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(9)
    public void deleteCategory_return200() {
        RestAssured.given()
                .header(this.authorizationHeader)
                .accept(ContentType.JSON)
                .pathParam("categoryId", CATEGORY_ID_1.toString())
                .delete(Paths.DELETE_CATEGORY)
                .then()
                .statusCode(HttpStatus.OK.value());

        //Check if category is deleted
        RestAssured.given()
                .pathParam("categoryId", CATEGORY_ID_1.toString())
                .accept(ContentType.JSON)
                .get(Paths.GET_CATEGORY)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    @Order(10)
    public void deleteCategory_CategoryNotExist_return404() {
        RestAssured.given()
                .header(this.authorizationHeader)
                .pathParam("categoryId", UUID.randomUUID().toString())
                .accept(ContentType.JSON)
                .delete(Paths.DELETE_CATEGORY)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(11)
    public void deleteCategory_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("categoryId", CATEGORY_ID_1.toString())
                .post(Paths.DELETE_CATEGORY)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private String createBookForCategory(CategoryId categoryId) {
        return RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Harry Potter\",\n" +
                        "  \"author\": \"Joanne K. Rowling\",\n" +
                        "  \"publisher\": \"Bloomsbury Publishing\",\n" +
                        "  \"publishingYear\": 1997,\n" +
                        "  \"categoryId\": \"" + categoryId.toString() + "\"\n" +
                        "}")
                .post(Paths.CREATE_BOOK)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getString("id");
    }
}