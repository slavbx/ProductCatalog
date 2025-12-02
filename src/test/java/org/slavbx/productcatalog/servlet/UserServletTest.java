package org.slavbx.productcatalog.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тестирование UserServlet")
class UserServletTest extends TestContainerTest {

    @Test
    @DisplayName("Проверка поиска пользователя по id")
    void doGetUserById() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("slav"));
    }

    @Test
    @DisplayName("Проверка поиска пользователя по email")
    void doGetUserByEmail() {
        given()
                .when()
                .get("/users/slav@slav.com")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("email", equalTo("slav@slav.com"))
                .body("name", equalTo("slav"));
    }

    @Test
    @DisplayName("Проверка получения всех пользователей")
    void doGetAllUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(1)));
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void doPost() {
        String userJson = """
            {
                "email": "testuser@test.com",
                "password": "testpass",
                "name": "Test User"
            }
            """;

        given()
                .contentType("application/json")
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("email", equalTo("testuser@test.com"))
                .body("name", equalTo("Test User"));
    }

    @Test
    @DisplayName("Проверка ошибки создания пользователя при существующем email")
    void doPost_WhenUserAlreadyExists() {
        String userJson = """
            {
                "email": "duplicate@test.com",
                "password": "testpass",
                "name": "Duplicate User"
            }
            """;

        given()
                .contentType("application/json")
                .body(userJson)
                .post("/users");

        given()
                .contentType("application/json")
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(409)
                .contentType("application/json")
                .body("message", containsString("already exists"));
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    void doPut() {
        String createJson = """
            {
                "email": "updateuser@test.com",
                "password": "original",
                "name": "Original Name"
            }
            """;

        given()
                .contentType("application/json")
                .body(createJson)
                .post("/users");

        String updateJson = """
            {
                "email": "updateuser@test.com",
                "password": "updated",
                "name": "Updated Name"
            }
            """;

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("email", equalTo("updateuser@test.com"))
                .body("name", equalTo("Updated Name"));
    }

    @Test
    @DisplayName("Проверка ошибки сброса пароля несуществующего пользователя")
    void doResetPassword_WhenUserNotFound() {
        given()
                .when()
                .put("/users/nonexistent@test.com/reset-password")
                .then()
                .statusCode(404)
                .contentType("application/json")
                .body("message", containsString("not found"));
    }
}