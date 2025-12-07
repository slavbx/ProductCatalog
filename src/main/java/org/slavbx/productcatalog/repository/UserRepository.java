package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения сущности пользователя {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {

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

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
