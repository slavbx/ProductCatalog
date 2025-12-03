package org.slavbx.productcatalog;

import jakarta.annotation.PostConstruct;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

/**
 * Суперкласс с описанием контейнера PostgreSQL для тестирования с помощью Testcontainers.
 * Инициализирует и запускает контейнер PostgreSQL при загрузке класса,
 * а также устанавливает системные свойства для использования в тестах и
 * вызывает инициализацию базы данных.
 */
@Configuration
@TestPropertySource("classpath:application-test.yml")
@ComponentScan("org.slavbx.productcatalog")
public class TestContainerConfig {

    public static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.database-name:test_db}")
    private String databaseName;

    @PostConstruct
    public void init() {
        if (POSTGRES_CONTAINER == null) {
            POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:13.3")
                    .withDatabaseName(databaseName)
                    .withUsername(username)
                    .withPassword(password);
            POSTGRES_CONTAINER.start();
            System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        }
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES_CONTAINER.getJdbcUrl());
        dataSource.setUser(POSTGRES_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRES_CONTAINER.getPassword());
        return dataSource;
    }

    @Bean
    public MockServletContext servletContext() {
        return new MockServletContext("");
    }
}
