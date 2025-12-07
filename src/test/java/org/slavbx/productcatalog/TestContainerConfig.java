package org.slavbx.productcatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.utility.DockerImageName;

/**
 * Суперкласс с описанием контейнера PostgreSQL для тестирования с помощью Testcontainers.
 * Инициализирует и запускает контейнер PostgreSQL при загрузке класса.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestContainerConfig {


    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("test_db")
                .withUsername("test")
                .withPassword("test");
    }
}