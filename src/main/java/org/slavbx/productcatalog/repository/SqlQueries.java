package org.slavbx.productcatalog.repository;

/**
 * Класс, содержащий константы SQL-запросов для работы с таблицами базы данных.
 */
public class SqlQueries {
    public static final String INSERT_USER =
            "INSERT INTO users (password, name, level, email) VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (email) DO UPDATE SET password = excluded.password, " +
            "name = excluded.name, level = excluded.level, email = excluded.email;";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    public static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ?;";
    public static final String DELETE_USER_BY_EMAIL = "DELETE FROM users WHERE email = ?;";
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
    public static final String SELECT_USERS = "SELECT * FROM users;";

    public static final String INSERT_BRAND =
            "INSERT INTO brands (name, description) VALUES (?, ?) ON CONFLICT (name) DO UPDATE SET description = excluded.description;";
    public static final String SELECT_BRAND_BY_NAME = "SELECT * FROM brands WHERE name = ?;";
    public static final String DELETE_BRAND_BY_NAME = "DELETE FROM brands WHERE name = ?;";
    public static final String SELECT_BRAND_BY_ID = "SELECT * FROM brands WHERE id = ?;";
    public static final String SELECT_BRANDS = "SELECT * FROM brands;";

    public static final String INSERT_CATEGORY =
            "INSERT INTO categories (name, description) VALUES (?, ?) ON CONFLICT (name) DO UPDATE SET description = excluded.description;";
    public static final String SELECT_CATEGORY_BY_NAME = "SELECT * FROM categories WHERE name = ?;";
    public static final String DELETE_CATEGORY_BY_NAME = "DELETE FROM categories WHERE name = ?;";
    public static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?;";
    public static final String SELECT_CATEGORIES = "SELECT * FROM categories;";

    public static final String INSERT_PRODUCT =
            "INSERT INTO products (name, description, price, quantity, create_date, user_id, category_id, brand_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT (name) DO UPDATE SET description = excluded.description, price = excluded.price, " +
            "quantity = excluded.quantity, create_date = excluded.create_date, user_id = excluded.user_id, " +
            "category_id = excluded.category_id, brand_id = excluded.brand_id;";
    public static final String SELECT_PRODUCT_BY_NAME =
            "SELECT p.*, " +
            "u.name AS user_name, u.email AS user_email, u.password AS user_password, u.level AS user_level, " +
            "c.name AS category_name, c.description AS category_description, " +
            "b.name AS brand_name, b.description AS brand_description " +
            "FROM products p " +
            "JOIN users u ON p.user_id = u.id " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN brands b ON p.brand_id = b.id " +
            "WHERE p.name = ?;";
    public static final String DELETE_PRODUCT_BY_NAME = "DELETE FROM products WHERE name = ?;";
    public static final String SELECT_PRODUCT_BY_ID =
            "SELECT p.*, " +
            "u.name AS user_name, u.email AS user_email, u.password AS user_password, u.level AS user_level, " +
            "c.name AS category_name, c.description AS category_description, " +
            "b.name AS brand_name, b.description AS brand_description " +
            "FROM products p " +
            "JOIN users u ON p.user_id = u.id " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN brands b ON p.brand_id = b.id " +
            "WHERE p.id = ?;";
    public static final String SELECT_PRODUCTS =
            "SELECT p.*, " +
            "u.name AS user_name, u.email AS user_email, u.password AS user_password, u.level AS user_level, " +
            "c.name AS category_name, c.description AS category_description, " +
            "b.name AS brand_name, b.description AS brand_description " +
            "FROM products p " +
            "JOIN users u ON p.user_id = u.id " +
            "JOIN categories c ON p.category_id = c.id " +
            "JOIN brands b ON p.brand_id = b.id " +
            "WHERE p.user_id = ?;";
}
