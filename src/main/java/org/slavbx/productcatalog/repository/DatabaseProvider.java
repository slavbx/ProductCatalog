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
    private static final String PROPERTY_PATH = "src/main/resources/application.properties";
    private static final String TEST_PROPERTY_PATH = "src/test/resources/application.properties";
    private static final String PROPERTY_DB_URL = "database.url";
    private static final String PROPERTY_DB_NAME = "database.name";
    private static final String PROPERTY_DB_USER = "database.username";
    private static final String PROPERTY_DB_PSW = "database.password";
    private static final String PROPERTY_LB_URL = "liquibase.url";
    private static final String PROPERTY_LB_USER = "liquibase.username";
    private static final String PROPERTY_LB_PSW = "liquibase.password";
    private static final String PROPERTY_LB_SCHEMA = "liquibase.defaultSchemaName";
    private static final String PROPERTY_LB_CHANGELOG = "liquibase.changeLogFile";

    /**
     * Предоставляет соединение с базой данных в зависимости от окружения (dev или test).
     * @return {@link Connection} — соединение с базой данных.
     * @throws SQLException если не удалось подключиться к базе данных.
     */
    public static Connection getConnection() throws SQLException {
        Connection connection;
        Properties properties = new Properties();

        try {
            InputStream in = getPropertiesInputStream();
            properties.load(in);
            Class.forName("org.postgresql.Driver");
            if (System.getProperty("env").equalsIgnoreCase("dev")) {
                connection = DriverManager.getConnection(
                        properties.getProperty(PROPERTY_DB_URL),
                        properties.getProperty(PROPERTY_DB_USER),
                        properties.getProperty(PROPERTY_DB_PSW)
                );
            } else {
                String test_url = "jdbc:postgresql://localhost:" + System.getProperty("test_port") + "/" +
                        properties.getProperty(PROPERTY_DB_NAME) +
                        "?currentSchema=pc_schema";
                connection = DriverManager.getConnection(
                        test_url,
                        properties.getProperty(PROPERTY_DB_USER),
                        properties.getProperty(PROPERTY_DB_PSW)
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
    public static void initDatabase() throws SQLException {
        Connection connection;
        Properties properties = new Properties();

        try {
            InputStream in = getPropertiesInputStream();
            properties.load(in);
            Class.forName("org.postgresql.Driver");
            if (System.getProperty("env").equalsIgnoreCase("dev")) {
                connection = DriverManager.getConnection(
                        properties.getProperty(PROPERTY_LB_URL),
                        properties.getProperty(PROPERTY_LB_USER),
                        properties.getProperty(PROPERTY_LB_PSW)
                );
            } else {
                String test_url = "jdbc:postgresql://localhost:" +
                        System.getProperty("test_port") + "/" +
                        properties.getProperty(PROPERTY_DB_NAME);
                connection = DriverManager.getConnection(
                        test_url,
                        properties.getProperty(PROPERTY_LB_USER),
                        properties.getProperty(PROPERTY_LB_PSW)
                );
            }
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(properties.getProperty(PROPERTY_LB_SCHEMA));

            try (Statement statement = connection.createStatement()) {
                String sql = String.format(CREATE_SCHEMA, properties.getProperty(PROPERTY_LB_SCHEMA));
                statement.execute(sql);
            } catch (SQLException e) {
                System.err.println("Error create default schema: " + e.getMessage());
            }

            Liquibase liquibase = new Liquibase(properties.getProperty(PROPERTY_LB_CHANGELOG),
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            connection.close();
        } catch (Exception e) {
            throw new SQLException("Initialising database failed");
        }
    }

    private static InputStream getPropertiesInputStream() throws Exception {
        String path = System.getProperty("env").equalsIgnoreCase("dev") ? PROPERTY_PATH : TEST_PROPERTY_PATH;
        InputStream classpathInput = DatabaseProvider.class.getClassLoader()
                .getResourceAsStream("application.properties");
        if (classpathInput != null) {
            return classpathInput;
        }
        return Files.newInputStream(Paths.get(path));
    }
}
