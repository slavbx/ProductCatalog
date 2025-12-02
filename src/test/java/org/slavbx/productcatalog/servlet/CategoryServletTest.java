package org.slavbx.productcatalog.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Тестирование CategoryServlet")
class CategoryServletTest extends TestContainerTest {

    @Test
    @DisplayName("Проверка поиска категории по id")
    void doGetCategoryById() {
        given()
                .when()
                .get("/categories/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("Electronics"));
    }

    @Test
    @DisplayName("Проверка поиска категории по name")
    void doGetCategoryByName() {
        given()
                .when()
                .get("/categories/Electronics")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("desc", equalTo("Электронные устройства и аксессуары"));
    }

    @Test
    @DisplayName("Проверка получения всех категорий")
    void doGetAllCategories() {
        given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(1)));
    }

    @Test
    @DisplayName("Проверка создания категории")
    void doPost() {
        String categoryJson = """
            {
                "name": "TestCategory",
                "desc": "Test Description"
            }
            """;

        given()
                .contentType("application/json")
                .body(categoryJson)
                .when()
                .post("/categories")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("name", equalTo("TestCategory"))
                .body("desc", equalTo("Test Description"));
    }

    @Test
    @DisplayName("Проверка ошибки создания категории при существующей")
    void doPost_WhenCategoryAlreadyExists() {
        String categoryJson = """
            {
                "name": "DuplicateCategory",
                "desc": "First creation"
            }
            """;

        given()
                .contentType("application/json")
                .body(categoryJson)
                .post("/categories");

        given()
                .contentType("application/json")
                .body(categoryJson)
                .when()
                .post("/categories")
                .then()
                .statusCode(409)
                .contentType("application/json")
                .body("message", containsString("already exists"));
    }

    @Test
    @DisplayName("Проверка обновления категории")
    void doPut() {
        String createJson = """
            {
                "name": "UpdateCategory",
                "desc": "Original Description"
            }
            """;

        given()
                .contentType("application/json")
                .body(createJson)
                .post("/categories");

        String updateJson = """
            {
                "name": "UpdateCategory",
                "desc": "Updated Description"
            }
            """;

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/categories")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("UpdateCategory"))
                .body("desc", equalTo("Updated Description"));
    }

    @Test
    @DisplayName("Проверка удаления категории")
    void doDelete() {
        String categoryJson = """
            {
                "name": "DeleteCategory",
                "desc": "To be deleted"
            }
            """;

        given()
                .contentType("application/json")
                .body(categoryJson)
                .post("/categories");

        given()
                .when()
                .delete("/categories/DeleteCategory")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("message", equalTo("Successfully deleted"));

        given()
                .when()
                .get("/categories/DeleteCategory")
                .then()
                .statusCode(404);
    }
}
