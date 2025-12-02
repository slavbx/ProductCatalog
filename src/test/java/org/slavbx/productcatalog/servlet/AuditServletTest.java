package org.slavbx.productcatalog.servlet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тестирование AuditServlet")
class AuditServletTest extends TestContainerTest {

    @BeforeAll
    static void setupRestAssured() {
        createTestAuditRecords();
    }

    private static void createTestAuditRecords() {
        createAuditRecord("test1@test.com", "CREATE_USER", LocalDateTime.now().minusHours(2));
        createAuditRecord("test1@test.com", "UPDATE_USER", LocalDateTime.now().minusHours(1));
        createAuditRecord("test2@test.com", "CREATE_PRODUCT", LocalDateTime.now().minusMinutes(30));
        createAuditRecord("test3@test.com", "DELETE_BRAND", LocalDateTime.now().minusMinutes(15));
    }

    private static void createAuditRecord(String email, String action, LocalDateTime dateTime) {
        String auditJson = String.format("""
        {
            "email": "%s",
            "action": "%s",
            "dateTime": "%s"
        }
        """, email, action, dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        given()
                .contentType("application/json")
                .body(auditJson)
                .post("/audit")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Проверка поиска аудит-записи по id")
    void doGetAuditRecordById() {
        given()
                .when()
                .get("/audit/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(1))
                .body("action", not(emptyOrNullString()))
                .body("email", not(emptyOrNullString()))
                .body("dateTime", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("Проверка поиска аудит-записей по email")
    void doGetAuditRecordsByEmail() {
        given()
                .when()
                .get("/audit/audittest@test.com")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(0)))
                .body("email", everyItem(equalTo("audittest@test.com")));
    }

    @Test
    @DisplayName("Проверка поиска аудит-записей по диапазону дат")
    void doGetAuditRecordsByDateRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(1);
        LocalDateTime endDate = now.plusDays(1);

        given()
                .queryParam("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .queryParam("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .when()
                .get("/audit")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(greaterThan(0)));
    }

    @Test
    @DisplayName("Проверка ошибки при отсутствии параметров дат")
    void doGetAuditRecordsByDateRange_WhenMissingParams() {
        given()
                .when()
                .get("/audit")
                .then()
                .statusCode(400)
                .contentType("application/json")
                .body("message", containsString("Parameters 'startDate' and 'endDate' are required"));
    }
}
