package org.slavbx.productcatalog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Level;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.UserRepository;
import org.slavbx.productcatalog.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Предоставляет функционал для работы с пользователями, включая
 * получение пользователей по id и email, создание пользователей,
 * а также получение текущего пользователя
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User save(User user) {
        if (user.getLevel() == null) user.setLevel(Level.USER);
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User create(User user) throws AlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("User with email: " + user.getEmail() + " already exists");
        }
        if (userRepository.existsByName(user.getName())) {
            throw new AlreadyExistsException("User with name: " + user.getName() + " already exists");
        }
        if (user.getLevel() == null) user.setLevel(Level.USER);
        return save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword(String email) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        existingUser.setPassword("psw");
        save(existingUser);
    }
}

