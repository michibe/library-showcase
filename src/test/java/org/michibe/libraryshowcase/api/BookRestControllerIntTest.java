package org.michibe.libraryshowcase.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class BookRestControllerIntTest extends BaseIntTest {

    private static BookId BOOK_ID_1;
    private static BookId BOOK_ID_2;
    private static CategoryId CATEGORY_ID_1;

    @BeforeAll
    public void setupSomeTestData() {
        final var response = RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Adventure\",\n" +
                        "  \"description\": \"Interesting\"\n" +
                        "}")
                .post(Paths.CREATE_CATEGORY)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var categoryId = response.getString("id");
        CATEGORY_ID_1 = CategoryId.ofString(categoryId);
    }


    @Test
    @Order(1)
    public void postBook_return200WithCreatedBook() {
        final var response = RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Harry Potter\",\n" +
                        "  \"author\": \"Joanne K. Rowling\",\n" +
                        "  \"publisher\": \"Bloomsbury Publishing\",\n" +
                        "  \"publishingYear\": 1997,\n" +
                        "  \"categoryId\": \"" + CATEGORY_ID_1.toString() + "\"\n" +
                        "}")
                .post(Paths.CREATE_BOOK)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var bookId = response.getString("id");
        assertThat(bookId).isNotEmpty();
        assertThat(response.getString("title")).isEqualTo("Harry Potter");
        assertThat(response.getString("author")).isEqualTo("Joanne K. Rowling");
        assertThat(response.getString("publisher")).isEqualTo("Bloomsbury Publishing");
        assertThat(response.getInt("publishingYear")).isEqualTo(1997);
        assertThat(response.getString("categoryId")).isNull();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_BOOK.replace("{bookId}", bookId));

        BOOK_ID_1 = BookId.ofString(bookId);
    }

    @Test
    @Order(2)
    public void postBook__notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(Paths.CREATE_BOOK)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    @Order(3)
    public void postBook_categoryIsNull_return200WithCreatedBook() {
        final var response = RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Harry Potter\",\n" +
                        "  \"author\": \"Joanne K. Rowling\",\n" +
                        "  \"publisher\": \"Bloomsbury Publishing\",\n" +
                        "  \"publishingYear\": 1997\n" +
                        "}")
                .post(Paths.CREATE_BOOK)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var bookId = response.getString("id");
        assertThat(bookId).isNotEmpty();
        assertThat(response.getString("title")).isEqualTo("Harry Potter");
        assertThat(response.getString("author")).isEqualTo("Joanne K. Rowling");
        assertThat(response.getString("publisher")).isEqualTo("Bloomsbury Publishing");
        assertThat(response.getInt("publishingYear")).isEqualTo(1997);
        assertThat(response.getString("categoryId")).isNull();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_BOOK.replace("{bookId}", bookId));

        BOOK_ID_2 = BookId.ofString(bookId);
    }

    @Test
    @Order(4)
    public void postBook_categoryDoesNotExist_return428() {
        RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Harry Potter\",\n" +
                        "  \"author\": \"Joanne K. Rowling\",\n" +
                        "  \"publisher\": \"Bloomsbury Publishing\",\n" +
                        "  \"publishingYear\": 1997,\n" +
                        "  \"categoryId\": \"" + UUID.randomUUID() + "\"\n" +
                        "}")
                .post(Paths.CREATE_BOOK)
                .then()
                .statusCode(HttpStatus.PRECONDITION_REQUIRED.value())
                .extract().body().jsonPath();
    }

    @Test
    @Order(5)
    public void getBook_return200WithRequestedBook() {
        final var response = RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("bookId", BOOK_ID_1.toString())
                .get(Paths.GET_BOOK)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var id = response.getString("id");
        assertThat(id).isEqualTo(BOOK_ID_1.toString());
        assertThat(response.getString("title")).isEqualTo("Harry Potter");
        assertThat(response.getString("author")).isEqualTo("Joanne K. Rowling");
        assertThat(response.getString("publisher")).isEqualTo("Bloomsbury Publishing");
        assertThat(response.getInt("publishingYear")).isEqualTo(1997);
        assertThat(response.getString("categoryId")).isNull();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_BOOK.replace("{bookId}", id));
    }

    @Test
    @Order(6)
    public void getBook_bookNotExist_return404() {
        RestAssured.given()
                .pathParam("bookId", UUID.randomUUID().toString())
                .accept(ContentType.JSON)
                .get(Paths.GET_BOOK)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(7)
    public void getBook_invalidBookId_return400() {
        RestAssured.given()
                .pathParam("bookId", "12344234")
                .accept(ContentType.JSON)
                .get(Paths.GET_BOOK)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(8)
    public void getAllBooks_return200WithRequestedBooks() {
        final var response = RestAssured.given()
                .accept(ContentType.JSON)
                .get(Paths.GET_ALL_BOOKS)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        assertThat(response.getList("_embedded.books")).hasSize(2);
    }

    @Test
    @Order(9)
    public void updateBook_return200WithUpdatedBook() {
        final var response = RestAssured.given()
                .header(this.authorizationHeader)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("bookId", BOOK_ID_1.toString())
                .body("{\n" +
                        "  \"title\": \"Harry Flopper\",\n" +
                        "  \"author\": \"Michael\",\n" +
                        "  \"publisher\": \"Holiday Publishing\",\n" +
                        "  \"publishingYear\": 2003\n" +
                        "}")
                .put(Paths.UPDATE_BOOK)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var bookId = response.getString("id");
        assertThat(bookId).isEqualTo(BOOK_ID_1.toString());
        assertThat(response.getString("title")).isEqualTo("Harry Flopper");
        assertThat(response.getString("author")).isEqualTo("Michael");
        assertThat(response.getString("publisher")).isEqualTo("Holiday Publishing");
        assertThat(response.getInt("publishingYear")).isEqualTo(2003);
        assertThat(response.getString("categoryId")).isNull();
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_BOOK.replace("{bookId}", bookId));
    }

    @Test
    @Order(10)
    public void updateBook_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("bookId", BOOK_ID_1.toString())
                .post(Paths.UPDATE_BOOK)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(11)
    public void deleteBook_return200() {
        RestAssured.given()
                .header(this.authorizationHeader)
                .accept(ContentType.JSON)
                .pathParam("bookId", BOOK_ID_1.toString())
                .delete(Paths.DELETE_BOOK)
                .then()
                .statusCode(HttpStatus.OK.value());

        //Check if book is deleted
        RestAssured.given()
                .pathParam("bookId", BOOK_ID_1.toString())
                .accept(ContentType.JSON)
                .get(Paths.GET_BOOK)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    @Order(12)
    public void deleteBook_bookNotExist_return404() {
        RestAssured.given()
                .header(this.authorizationHeader)
                .pathParam("bookId", UUID.randomUUID().toString())
                .accept(ContentType.JSON)
                .delete(Paths.DELETE_BOOK)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(13)
    public void deleteBook_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("bookId", BOOK_ID_1.toString())
                .post(Paths.DELETE_BOOK)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}