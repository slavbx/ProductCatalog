package org.slavbx.productcatalog.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * Класс для предоставления подключения к базе данных.
 */
public class DatabaseProvider {
    private static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS %s";
    private static final String RESOURCES_PATH = "src/main/resources/";

    /**
     * Предоставляет соединение с базой данных в зависимости от окружения (dev или test).
     * @return {@link Connection} — соединение с базой данных.
     * @throws SQLException если не удалось подключиться к базе данных.
     */
    public static Connection getConnection() throws SQLException {
        Connection connection;
            Properties properties = new Properties();
            try {
                InputStream in = Files.newInputStream(Paths.get(RESOURCES_PATH + "database.properties"));
                properties.load(in);
                if (System.getProperty("env").equalsIgnoreCase("dev")) {
                    connection = DriverManager.getConnection(
                            properties.getProperty("database.url"),
                            properties.getProperty("database.username"),
                            properties.getProperty("database.password")
                    );
                } else {
                    String test_db = properties.getProperty("database-test.name");
                    String test_url = "jdbc:postgresql://localhost:" + System.getProperty("test_port") + "/" + test_db + "?currentSchema=pc_schema";
                    connection = DriverManager.getConnection(
                            test_url,
                            properties.getProperty("database-test.username"),
                            properties.getProperty("database-test.password")
                    );
                }
            } catch (Exception e) {
                throw new SQLException("Connection to database failed");
            }
        return connection;
    }

    /**
     * Инициализирует базу данных: создает схему и применяет изменения Liquibase.
     * @throws SQLException если при работе с базой данных возникла ошибка.
     */
    public static void initDatabase()  {
        Connection connection;
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(RESOURCES_PATH + "liquibase.properties"))) {
            properties.load(in);
            if (System.getProperty("env").equalsIgnoreCase("dev")) {
                connection = DriverManager.getConnection(
                        properties.getProperty("liquibase.url"),
                        properties.getProperty("liquibase.username"),
                        properties.getProperty("liquibase.password")
                );
            } else {
                String test_db = properties.getProperty("database-test.name");
                String test_url = "jdbc:postgresql://localhost:" + System.getProperty("test_port") + "/" + test_db;
                connection = DriverManager.getConnection(
                        test_url,
                        properties.getProperty("database-test.username"),
                        properties.getProperty("database-test.password")
                );
            }
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(properties.getProperty("liquibase.defaultSchemaName"));

            try (Statement statement = connection.createStatement()) {
                String sql = String.format(CREATE_SCHEMA, properties.getProperty("liquibase.defaultSchemaName"));
                statement.execute(sql);
            } catch (SQLException e) {
                System.err.println("Error create default schema: " + e.getMessage());
            }

            Liquibase liquibase = new Liquibase(properties.getProperty("liquibase.changeLogFile"),
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
