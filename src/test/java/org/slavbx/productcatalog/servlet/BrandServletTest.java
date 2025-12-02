package org.slavbx.productcatalog.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тестирование BrandServlet")
class BrandServletTest extends TestContainerTest {

    @Test
    @DisplayName("Проверка поиска бренда по id")
    void doGetBrandById() {
        given()
                .when()
                .get("/brands/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("Zalman"));
    }

    @Test
    @DisplayName("Проверка поиска бренда по name")
    void doGetBrandByName() {
        given()
                .when()
                .get("/brands/Zalman")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("desc", equalTo("Производитель систем охлаждения и корпусов"));
    }

    @Test
    @DisplayName("Проверка получения всех брендов")
    void doGetAllBrands() {
        given()
                .when()
                .get("/brands")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(1)));
    }

    @Test
    @DisplayName("Проверка создания бренда")
    void doPost() {
        String brandJson = """
            {
                "name": "TestBrand",
                "desc": "Test Description"
            }
            """;

        given()
                .contentType("application/json")
                .body(brandJson)
                .when()
                .post("/brands")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("name", equalTo("TestBrand"))
                .body("desc", equalTo("Test Description"));
    }

    @Test
    @DisplayName("Проверка ошибки создания бренда при существующем")
    void doPost_WhenBrandAlreadyExists() {
        String brandJson = """
            {
                "name": "DuplicateBrand",
                "desc": "First creation"
            }
            """;

        given()
                .contentType("application/json")
                .body(brandJson)
                .post("/brands");

        given()
                .contentType("application/json")
                .body(brandJson)
                .when()
                .post("/brands")
                .then()
                .statusCode(409)
                .contentType("application/json")
                .body("message", containsString("already exists"));
    }

    @Test
    @DisplayName("Проверка обновления бренда")
    void doPut() {
        String createJson = """
            {
                "name": "UpdateBrand",
                "desc": "Original Description"
            }
            """;

        String createdBrand = given()
                .contentType("application/json")
                .body(createJson)
                .post("/brands")
                .then()
                .extract()
                .toString();

        String updateJson = """
            {
                "name": "UpdateBrand",
                "desc": "Updated Description"
            }
            """;

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/brands")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("UpdateBrand"))
                .body("desc", equalTo("Updated Description"));
    }

    @Test
    @DisplayName("Проверка удаления бренда")
    void doDelete() {
        String brandJson = """
            {
                "name": "DeleteBrand",
                "desc": "To be deleted"
            }
            """;

        given()
                .contentType("application/json")
                .body(brandJson)
                .post("/brands");

        given()
                .when()
                .delete("/brands/DeleteBrand")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("message", equalTo("Successfully deleted"));

        given()
                .when()
                .get("/brands/DeleteBrand")
                .then()
                .statusCode(404);
    }
}