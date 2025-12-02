package org.slavbx.productcatalog;

import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.servlet.*;
import org.testcontainers.containers.PostgreSQLContainer;
import io.restassured.RestAssured;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Суперкласс с описанием контейнера PostgreSQL для тестирования с помощью Testcontainers.
 * Инициализирует и запускает контейнер PostgreSQL при загрузке класса,
 * а также устанавливает системные свойства для использования в тестах и
 * вызывает инициализацию базы данных.
 */
public class TestContainerTest {
    public static PostgreSQLContainer<?> POSTGRES_CONTAINER;
    public static Server JETTY_SERVER;

    static {
        try {
            Properties properties = new Properties();
            InputStream in = Files.newInputStream(Paths.get("src/test/resources/application.properties"));
            properties.load(in);
            POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:13.3")
                    .withDatabaseName(properties.getProperty("database.name"))
                    .withUsername(properties.getProperty("database.username"))
                    .withPassword(properties.getProperty("database.password"))
                    .withCreateContainerCmdModifier(cmd -> cmd.withName("testcontainers-postgresql"));
            POSTGRES_CONTAINER.start();
            //Передача состояния профиля тестирования и порта классу DatabaseProvider
            System.setProperty("repository.type", "JDBC");
            System.setProperty("env", "test");
            System.setProperty("test_port", String.valueOf(POSTGRES_CONTAINER.getFirstMappedPort()));
            DatabaseProvider.initDatabase();

            startJettyServer();

            RestAssured.baseURI = "http://localhost";
            RestAssured.port = 8080;

        } catch (Exception e) {
            throw new RuntimeException("Start testcontainer or server failed");
        }
    }

    private static void startJettyServer() throws Exception {
        JETTY_SERVER = new Server(8080);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        context.addServlet(BrandServlet.class, "/brands/*");
        context.addServlet(CategoryServlet.class, "/categories/*");
        context.addServlet(ProductServlet.class, "/products/*");
        context.addServlet(UserServlet.class, "/users/*");
        context.addServlet(AuthServlet.class, "/auth/*");
        context.addServlet(AuditServlet.class, "/audit/*");

        JETTY_SERVER.setHandler(context);
        JETTY_SERVER.start();

        Thread.sleep(1000);
        System.out.println("Jetty server started on port: " + 8080);
    }

    public void start() {
        POSTGRES_CONTAINER.start();
    }

    public void stop() {
        POSTGRES_CONTAINER.stop();
        try {
            JETTY_SERVER.stop();
        } catch (Exception e) {
            System.err.println("Error stopping Jetty Server: " + e.getMessage());
        }
    }
}
