package org.slavbx.productcatalog;

import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Конфигурация контейнера PostgreSQL для тестирования с помощью Testcontainers.
 * Инициализирует и запускает контейнер PostgreSQL при загрузке класса,
 * а также устанавливает системные свойства для использования в тестах и
 * вызывает инициализацию базы данных.
 */
public class TestContainerConfig {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        try {
            Properties properties = new Properties();
            InputStream in = Files.newInputStream(Paths.get("src/main/resources/database.properties"));
            properties.load(in);
            POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:13.3")
                    .withDatabaseName(properties.getProperty("database-test.name"))
                    .withUsername(properties.getProperty("database-test.username"))
                    .withPassword(properties.getProperty("database-test.password"));
            POSTGRES_CONTAINER.start();
            //Передача состояния профиля тестирования и порта классу DatabaseProvider
            System.setProperty("env", "test");
            System.setProperty("test_port", String.valueOf(POSTGRES_CONTAINER.getFirstMappedPort()));
            DatabaseProvider.initDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJdbcUrl() {
        return POSTGRES_CONTAINER.getJdbcUrl();
    }
}
