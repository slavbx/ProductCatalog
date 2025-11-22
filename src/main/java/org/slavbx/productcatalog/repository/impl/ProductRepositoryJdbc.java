package org.slavbx.productcatalog.repository.impl;

import org.slavbx.productcatalog.exception.RepositoryException;
import org.slavbx.productcatalog.model.*;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.ProductRepository;
import org.slavbx.productcatalog.repository.SqlQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для хранения товаров
 */
public class ProductRepositoryJdbc implements ProductRepository {
    @Override
    public Product save(Product product) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_PRODUCT);
            prep.setString(1, product.getName());
            prep.setString(2, product.getDesc());
            prep.setBigDecimal(3, product.getPrice());
            prep.setLong(4, product.getQuantity());
            if (product.getCreateDate() != null) prep.setString(5, product.getCreateDate().toString());
            if (product.getSeller() != null) prep.setLong(6, product.getSeller().getId());
            if (product.getCategory() != null) prep.setLong(7, product.getCategory().getId());
            if (product.getBrand() != null) prep.setLong(8, product.getBrand().getId());
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось сохранить товар " + e.getMessage(), e);
        }
        return product;
    }

    @Override
    public void deleteByName(String name) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_PRODUCT_BY_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось удалить товар"  + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> findByName(String name) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_PRODUCT_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось вернуть товар"  + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findById(Long id) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_PRODUCT_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось вернуть товар"  + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Product> findAllProductsByUser(User user) throws RepositoryException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_PRODUCTS);
            preparedStatement.setLong(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось вернуть товары"  + e.getMessage(), e);
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        User seller = User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("user_name"))
                .email(resultSet.getString("user_email"))
                .password(resultSet.getString("user_password"))
                .level(Level.valueOf(resultSet.getString("user_level")))
                .build();
        Category category = Category.builder()
                .id(resultSet.getLong("category_id"))
                .name(resultSet.getString("category_name"))
                .desc(resultSet.getString("category_description"))
                .build();
        Brand brand = Brand.builder()
                .id(resultSet.getLong("brand_id"))
                .name(resultSet.getString("brand_name"))
                .desc(resultSet.getString("brand_description"))
                .build();
        return Product.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .desc(resultSet.getString("description"))
                .price(resultSet.getBigDecimal("price"))
                .quantity(resultSet.getInt("quantity"))
                .createDate(resultSet.getDate("create_date").toLocalDate())
                .seller(seller)
                .category(category)
                .brand(brand)
                .build();
    }
}
