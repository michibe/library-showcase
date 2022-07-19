package org.michibe.libraryshowcase.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.michibe.libraryshowcase.api.model.CreateUserReqPayload;
import org.michibe.libraryshowcase.modules.user.UserService;
import org.michibe.libraryshowcase.modules.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles(profiles = "test")
class BaseIntTest {
    protected Header authorizationHeader;
    @LocalServerPort
    private int port;
    @Autowired
    private UserService userService;

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        userService.createUser(new CreateUserReqPayload(
                "TestUser",
                "testemail@test.de",
                "123",
                Role.CUSTOMER
        ));

        final var jwt = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\n" +
                        "  \"email\": \"testemail@test.de\",\n" +
                        "  \"password\": \"123\"\n" +
                        "}")
                .post(Paths.CREATE_ACCESS_TOKEN)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getString("jwt");

        authorizationHeader = new Header("Authorization", "Bearer " + jwt);
    }


}