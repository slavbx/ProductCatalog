package org.slavbx.productcatalog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.exception.RepositoryException;
import org.slavbx.productcatalog.model.Level;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.SqlQueries;
import org.slavbx.productcatalog.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для хранения пользователей
 */
@Primary
@Repository
@RequiredArgsConstructor
public class UserRepositoryJdbc implements UserRepository {
    private final DataSource dataSource;

    @Override
    public User save(User user) throws RepositoryException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_USER);
            prep.setString(1, user.getPassword());
            prep.setString(2, user.getName());
            prep.setString(3, user.getLevel().name());
            prep.setString(4, user.getEmail());
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to save user: " + e.getMessage(), e);
        }
        return user;
    }

    @Override
    public void deleteByEmail(String email) throws RepositoryException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.DELETE_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to delete user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws RepositoryException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) throws RepositoryException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) throws RepositoryException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_USER_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return user: " + e.getMessage(), e);
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
    public List<User> findAllUsers() throws RepositoryException {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlQueries.SELECT_USERS);
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return users: " + e.getMessage(), e);
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
