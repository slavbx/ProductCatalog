package org.slavbx.productcatalog.security;

import org.slavbx.productcatalog.model.User;

/**
 * Интерфейс для обеспечения аутентификации и авторизации пользователей.
 * Предоставляет функционал для регистрации нового пользователя,
 * а также аутентификации существующих пользователей
 */
public interface AuthenticationService {

    /**
     * Регистрация нового пользователя
     */
    void signIn(User user);

    /**
     * Аутентификация пользователя
     */
    void signOut();

    User getCurrentUser();

    void setCurrentUser(User user);
}
