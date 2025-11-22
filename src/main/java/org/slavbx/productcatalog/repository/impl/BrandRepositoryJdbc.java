package org.slavbx.productcatalog.repository.impl;

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
    public Brand save(Brand brand) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_BRAND);
            prep.setString(1, brand.getName());
            prep.setString(2, brand.getDesc());
            prep.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось сохранить бренд " + e.getMessage());
        }
        return brand;
    }

    @Override
    public void deleteByName(String name) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_BRAND_BY_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось удалить бренд"  + e.getMessage());
        }
    }

    @Override
    public Optional<Brand> findByName(String name) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_BRAND_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBrand(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось вернуть брэнд"  + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_BRAND_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBrand(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось вернуть бренд"  + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Brand> findAllBrands() {
        List<Brand> brands = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlQueries.SELECT_BRANDS);
            while (resultSet.next()) {
                brands.add(mapResultSetToBrand(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось найти бренды"  + e.getMessage());
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
