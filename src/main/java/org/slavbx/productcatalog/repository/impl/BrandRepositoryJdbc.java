package org.slavbx.productcatalog.repository.impl;

import org.slavbx.productcatalog.exception.RepositoryException;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.repository.BrandRepository;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.SqlQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для хранения брендов
 */
public class BrandRepositoryJdbc implements BrandRepository {
    @Override
    public Brand save(Brand brand) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_BRAND);
            prep.setString(1, brand.getName());
            prep.setString(2, brand.getDesc());
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to save brand: " + e.getMessage(), e);
        }
        return brand;
    }

    @Override
    public void deleteByName(String name) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_BRAND_BY_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to delete brand: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Brand> findByName(String name) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_BRAND_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBrand(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return brand: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Brand> findById(Long id) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_BRAND_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBrand(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return brand: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Brand> findAllBrands() throws RepositoryException {
        List<Brand> brands = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlQueries.SELECT_BRANDS);
            while (resultSet.next()) {
                brands.add(mapResultSetToBrand(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return brands: " + e.getMessage(), e);
        }
        return brands;
    }

    private Brand mapResultSetToBrand(ResultSet resultSet) throws SQLException {
        return Brand.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .desc(resultSet.getString("description"))
                .build();
    }
}
