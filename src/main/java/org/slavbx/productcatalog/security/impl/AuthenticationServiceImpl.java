package org.slavbx.productcatalog.security.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.UserRepository;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для обеспечения аутентификации и авторизации пользователей.
 * Предоставляет функционал для регистрации нового пользователя,
 * а также аутентификации существующих пользователей
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    @Getter
    @Setter
    public User currentUser = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void signIn(User user) throws NotFoundException {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new NotFoundException("User not found with email: " + user.getEmail()));
        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new NotFoundException("User with email: " + user.getEmail() + " wrong password");
        }
        this.setCurrentUser(existingUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signOut() {
        this.setCurrentUser(null);
    }
}
