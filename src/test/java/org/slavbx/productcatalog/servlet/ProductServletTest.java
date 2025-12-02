package org.slavbx.productcatalog.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тестирование ProductServlet")
class ProductServletTest extends TestContainerTest {

    @Test
    @DisplayName("Проверка поиска товара по id")
    void doGetProductById() {
        given()
                .when()
                .get("/products/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("Корпус ПК"));
    }

    @Test
    @DisplayName("Проверка создания товара")
    void doPost() {
        String productJson = """
            {
                "name": "TestProduct",
                "desc": "Test Description",
                "price": 1000.00,
                "quantity": 5,
                "sellerEmail": "slav@slav.com",
                "categoryName": "Electronics",
                "brandName": "Zalman"
            }
            """;

        given()
                .contentType("application/json")
                .body(productJson)
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("name", equalTo("TestProduct"))
                .body("desc", equalTo("Test Description"))
                .body("price", equalTo(1000.0f))
                .body("quantity", equalTo(5));
    }

    @Test
    @DisplayName("Проверка ошибки создания товара при существующем")
    void doPost_WhenProductAlreadyExists() {
        String productJson = """
            {
                "name": "DuplicateProduct",
                "desc": "First creation",
                "price": 1000.00,
                "quantity": 5,
                "sellerEmail": "slav@slav.com",
                "categoryName": "Electronics",
                "brandName": "Zalman"
            }
            """;

        given()
                .contentType("application/json")
                .body(productJson)
                .post("/products");

        given()
                .contentType("application/json")
                .body(productJson)
                .when()
                .post("/products")
                .then()
                .statusCode(409)
                .contentType("application/json")
                .body("message", containsString("already exists"));
    }

    @Test
    @DisplayName("Проверка обновления товара")
    void doPut() {
        String createJson = """
            {
                "name": "UpdateProduct",
                "desc": "Original Description",
                "price": 1000.00,
                "quantity": 5,
                "sellerEmail": "slav@slav.com",
                "categoryName": "Electronics",
                "brandName": "Zalman"
            }
            """;

        given()
                .contentType("application/json")
                .body(createJson)
                .post("/products");

        String updateJson = """
            {
                "name": "UpdateProduct",
                "desc": "Updated Description",
                "price": 1500.00,
                "quantity": 10,
                "sellerEmail": "slav@slav.com",
                "categoryName": "Electronics",
                "brandName": "Zalman"
            }
            """;

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/products")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("UpdateProduct"))
                .body("desc", equalTo("Updated Description"))
                .body("price", equalTo(1500.0f))
                .body("quantity", equalTo(10));
    }

    @Test
    @DisplayName("Проверка удаления товара")
    void doDelete() {
        String productJson = """
            {
                "name": "DeleteProduct",
                "desc": "To be deleted",
                "price": 1000.00,
                "quantity": 5,
                "sellerEmail": "slav@slav.com",
                "categoryName": "Electronics",
                "brandName": "Zalman"
            }
            """;

        given()
                .contentType("application/json")
                .body(productJson)
                .post("/products");

        given()
                .when()
                .delete("/products/DeleteProduct")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("message", equalTo("Successfully deleted"));

        given()
                .when()
                .get("/products/DeleteProduct")
                .then()
                .statusCode(404);
    }
}