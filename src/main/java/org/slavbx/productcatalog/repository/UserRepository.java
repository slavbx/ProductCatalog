package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения сущности пользователя {@link User}
 */
public interface UserRepository {
    /**
     * Сохраняет пользователя
     * @param user объект пользователя для сохранения
     */
    User save(User user);

    /**
     * Удаляет пользователя по указанному email
     * @param email электронная почта для удаления пользователя
     */
    void deleteByEmail(String email);

    /**
     * Находит пользователя по указанному email
     * @param email электронная почта для поиска пользователя
     * @return объект Optional, содержащий найденного пользователя, или пустой объект, если пользователь не найден
     */
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    /**
     * Находит пользователя по id
     * @param id идентификатор для поиска пользователя
     * @return объект Optional, содержащий найденного пользователя, или пустой объект, если пользователь не найден
     */
    Optional<User> findById(Long id);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    /**
     * Находит всех существующих пользователей
     * @return список пользователей
     */
    List<User> findAllUsers();
}
