package org.slavbx.productcatalog.service;

import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.User;

import java.util.List;

/**
 * Интерфейс для управления пользователями.
 * Предоставляет функционал для работы с пользователями, включая
 * получение пользователей по id и email, создание пользователей,
 * а также получение текущего пользователя
 */
public interface UserService {
    /**
     * Получение пользователя по id.
     *
     * @param id уникальный идентификатор пользователя.
     * @return объект User, соответствующий указанному идентификатору.
     * @throws NotFoundException если пользователь не найден
     */
    User getUserById(Long id) throws NotFoundException;

    /**
     * Получение пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты пользователя.
     * @return объект User, соответствующий указанному адресу электронной почты.
     * @throws NotFoundException если пользователь не найден
     */
    User getUserByEmail(String email) throws NotFoundException;

    /**
     * Сохранение пользователя.
     *
     * @param user объект User, который нужно сохранить.
     * @return сохраненный объект User
     */
    User save(User user);

    /**
     * Создание нового пользователя.
     *
     * @param user объект User, представляющий нового пользователя.
     * @return объект User, который был создан.
     * @throws AlreadyExistsException если пользователь с таким email или именем уже существует
     */
    User create(User user);

    /**
     * Получение текущего аутентифицированного пользователя.
     *
     * @return объект User, представляющий текущего пользователя
     */
    User getCurrentUser();

    /**
     * Возвращает список всех зарегистрированных пользователей.
     * @return список пользователей
     */
    List<User> findAllUsers();

    void signIn(User user);

    void signOut();

    void resetPassword(String email);
}
