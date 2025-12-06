package org.slavbx.productcatalog.repository;

import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Класс для предоставления подключения к базе данных
 */
@Component
public class DatabaseProvider {

    private final DataSource dataSource;
    private final String liquibaseSchema;
    private final String liquibaseChangeLog;

    @Autowired
    public DatabaseProvider(DataSource dataSource,
                            @Value("${spring.liquibase.default-schema}") String liquibaseSchema,
                            @Value("${spring.liquibase.change-log}") String liquibaseChangeLog) {
        this.dataSource = dataSource;
        this.liquibaseSchema = liquibaseSchema;
        this.liquibaseChangeLog = liquibaseChangeLog;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @PostConstruct
    public void initDatabase() {
        try (Connection connection = dataSource.getConnection()) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(liquibaseSchema);

            try (Statement statement = connection.createStatement()) {
                String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s", liquibaseSchema);
                statement.execute(sql);
            }

            Liquibase liquibase = new Liquibase(liquibaseChangeLog, new ClassLoaderResourceAccessor(), database);
            liquibase.update();

        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}