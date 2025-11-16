package org.slavbx.productcatalog.repository.impl;

import org.slavbx.productcatalog.model.Level;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.SqlQueries;
import org.slavbx.productcatalog.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для хранения пользователей
 */
public class UserRepositoryJdbc implements UserRepository {
    @Override
    public User save(User user) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_USER);
            prep.setString(1, user.getPassword());
            prep.setString(2, user.getName());
            prep.setString(3, user.getLevel().name());
            prep.setString(4, user.getEmail());
            prep.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось сохранить пользователя " + e.getMessage());
        }
        return user;
    }

    @Override
    public void deleteByEmail(String email) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось удалить пользователя"  + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось вернуть пользователя"  + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось вернуть пользователя"  + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось вернуть пользователя"  + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlQueries.SELECT_USERS);
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка. Не удалось найти пользователей"  + e.getMessage());
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .name(resultSet.getString("name"))
                .level(Level.valueOf(resultSet.getString("level")))
                .build();
    }
}
