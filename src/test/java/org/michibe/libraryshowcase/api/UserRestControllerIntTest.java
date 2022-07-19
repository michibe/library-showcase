package org.michibe.libraryshowcase.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class UserRestControllerIntTest extends BaseIntTest {

    private static UserId USER_ID_1;

    @Test
    @Order(1)
    public void postUser_return200WithCreatedUser() {
        final var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .body("{\n" +
                        "  \"name\": \"Max\",\n" +
                        "  \"email\": \"max-mustermann@gmail.com\",\n" +
                        "  \"password\": \"411442\",\n" +
                        "  \"role\": \"CUSTOMER\"\n" +
                        "}")
                .post(Paths.CREATE_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var userId = response.getString("id");
        assertThat(userId).isNotEmpty();
        assertThat(response.getString("name")).isEqualTo("Max");
        assertThat(response.getString("email")).isEqualTo("max-mustermann@gmail.com");
        assertThat(response.getString("role")).isEqualTo("CUSTOMER");
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_USER.replace("{userId}", userId));

        USER_ID_1 = UserId.ofString(userId);
    }

    @Test
    @Order(2)
    public void postUser_userWithEmailAlreaedyExist_return412() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .body("{\n" +
                        "  \"name\": \"Max\",\n" +
                        "  \"email\": \"max-mustermann@gmail.com\",\n" +
                        "  \"password\": \"411442\",\n" +
                        "  \"role\": \"CUSTOMER\"\n" +
                        "}")
                .post(Paths.CREATE_USER)
                .then()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    @Order(3)
    public void postUser_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{}")
                .post(Paths.CREATE_USER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(4)
    public void postUser_emailInvalid_return400() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .body("{\n" +
                        "  \"name\": \"Max\",\n" +
                        "  \"email\": \"max\",\n" +
                        "  \"password\": \"411442\",\n" +
                        "  \"role\": \"CUSTOMER\"\n" +
                        "}")
                .post(Paths.CREATE_USER)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(5)
    public void postUser_noEmailInvalid_return400() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .body("{\n" +
                        "  \"name\": \"Max\",\n" +
                        "  \"password\": \"411442\",\n" +
                        "  \"role\": \"CUSTOMER\"\n" +
                        "}")
                .post(Paths.CREATE_USER)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(6)
    public void getUser_return200WithRequestedUser() {
        final var response = RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("userId", USER_ID_1.toString())
                .header(this.authorizationHeader)
                .get(Paths.GET_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var id = response.getString("id");
        assertThat(id).isEqualTo(USER_ID_1.toString());
        assertThat(response.getString("name")).isEqualTo("Max");
        assertThat(response.getString("email")).isEqualTo("max-mustermann@gmail.com");
        assertThat(response.getString("role")).isEqualTo("CUSTOMER");
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_USER.replace("{userId}", id));
    }

    @Test
    @Order(7)
    public void getUser_notAuthorized_return403() {
        RestAssured.given()
                .pathParam("userId", UUID.randomUUID().toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(Paths.GET_USER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    @Order(8)
    public void getUser_userNotExist_return404() {
        RestAssured.given()
                .pathParam("userId", UUID.randomUUID().toString())
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .get(Paths.GET_USER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(9)
    public void getUser_invalidUserId_return400() {
        RestAssured.given()
                .pathParam("userId", "12344234")
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .get(Paths.GET_USER)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(10)
    public void getAllUsers_return200WithRequestedUsers() {
        final var response = RestAssured.given()
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .get(Paths.GET_ALL_USERS)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        assertThat(response.getList("_embedded.users")).hasSize(2);
        assertThat(response.getString("_embedded.users[0].name")).isNotEmpty();
    }

    @Test
    @Order(11)
    public void getAllUser_notAuthorized_return403() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(Paths.GET_ALL_USERS)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(12)
    public void updateUser_return200WithUpdatedUser() {
        final var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("userId", USER_ID_1.toString())
                .header(this.authorizationHeader)
                .body("{\n" +
                        "  \"name\": \"Muxx\",\n" +
                        "  \"password\": \"4444444\",\n" +
                        "  \"role\": \"CUSTOMER\"\n" +
                        "}")
                .put(Paths.UPDATE_USER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath();

        final var id = response.getString("id");
        assertThat(id).isEqualTo(USER_ID_1.toString());
        assertThat(response.getString("name")).isEqualTo("Muxx");
        assertThat(response.getString("email")).isEqualTo("max-mustermann@gmail.com");
        assertThat(response.getString("role")).isEqualTo("CUSTOMER");
        assertThat(response.getString("_links.self.href")).isEqualTo(Paths.GET_USER.replace("{userId}", id));
    }

    @Test
    @Order(13)
    public void updateUser_notAuthorized_return403() {
        RestAssured.given()
                .pathParam("userId", UUID.randomUUID().toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(Paths.UPDATE_USER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    @Order(14)
    public void deleteUser_return200() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .pathParam("userId", USER_ID_1.toString())
                .header(this.authorizationHeader)
                .delete(Paths.DELETE_USER)
                .then()
                .statusCode(HttpStatus.OK.value());

        //Check if user is deleted
        RestAssured.given()
                .pathParam("userId", USER_ID_1.toString())
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .get(Paths.GET_USER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    @Order(15)
    public void deleteUser_UserNotExist_return404() {
        RestAssured.given()
                .pathParam("userId", UUID.randomUUID().toString())
                .accept(ContentType.JSON)
                .header(this.authorizationHeader)
                .delete(Paths.DELETE_USER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(16)
    public void deleteUser_notAuthorized_return403() {
        RestAssured.given()
                .pathParam("userId", UUID.randomUUID().toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(Paths.DELETE_USER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}