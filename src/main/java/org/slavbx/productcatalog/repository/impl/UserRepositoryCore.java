package org.slavbx.productcatalog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Реализация репозитория для хранения сущности пользователя {@link UserRepository}
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryCore implements UserRepository {
    private Long lastId;

    private final Map<Long, User> users = new HashMap<>();

    public UserRepositoryCore() {
        this.lastId = 0L;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++lastId);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteByEmail(String email) {
        Long id = null;
        for (User user: users.values()) {
            if (user.getEmail().equals(email)) {
                id = user.getId();
                break;
            }
        }
        if (id != null) users.remove(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user: users.values()) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) {
        for (User user: users.values()) {
            if (user.getName().equals(name)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
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
        return new ArrayList<>(users.values());
    }
}
