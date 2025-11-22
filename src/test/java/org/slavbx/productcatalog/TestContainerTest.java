package org.slavbx.productcatalog;

import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.testcontainers.containers.PostgreSQLContainer;

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
            System.setProperty("env", "test");
            System.setProperty("test_port", String.valueOf(POSTGRES_CONTAINER.getFirstMappedPort()));
            DatabaseProvider.initDatabase();
        } catch (IOException |SQLException e ) {
            throw new RuntimeException("Start testcontainer failed");
        }
    }

    public void start() {
        POSTGRES_CONTAINER.start();
    }

    public void stop() {
        POSTGRES_CONTAINER.stop();
    }
}
