package org.slavbx.productcatalog.repository.impl;

import org.slavbx.productcatalog.exception.RepositoryException;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.repository.CategoryRepository;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.SqlQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для хранения категорий
 */
public class CategoryRepositoryJdbc implements CategoryRepository {
    @Override
    public Category save(Category category) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_CATEGORY);
            prep.setString(1, category.getName());
            prep.setString(2, category.getDesc());
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось сохранить категорию"  + e.getMessage(), e);
        }
        return category;
    }

    @Override
    public void deleteByName(String name) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_CATEGORY_BY_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось удалить категорию"  + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Category> findByName(String name) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_CATEGORY_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось вернуть категорию"  + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> findById(Long id) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_CATEGORY_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось вернуть категорию"  + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Category> findAllCategories() throws RepositoryException {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlQueries.SELECT_CATEGORIES);
            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка. Не удалось вернуть категории"  + e.getMessage(), e);
        }
        return categories;
    }

    private Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        return Category.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .desc(resultSet.getString("description"))
                .build();
    }
}
