package org.slavbx.productcatalog.repository.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.Level;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestContainerConfig.class)
@SpringBootTest
@Transactional
@DisplayName("Тестирование UserRepository")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    User user = User.builder().email("slav@slav.com").level(Level.USER).name("slav").password("slav").build();

    @Test
    @DisplayName("Проверка сохранения пользователя")
    void save() {
        User newUser1 = User.builder()
                .email("newuser1@newuser.com")
                .level(Level.USER)
                .name("newuser1")
                .password("newuser1")
                .build();
        assertThat(userRepository.save(newUser1)).isEqualTo(newUser1);
    }

    @Test
    @DisplayName("Проверка удаления пользователя по email")
    void deleteByEmail() {
        User newUser2 = User.builder()
                .email("newuser2@newuser.com")
                .level(Level.USER)
                .name("newuser2")
                .password("newuser2")
                .build();
        userRepository.save(newUser2);
        userRepository.deleteByEmail("newuser2@newuser.com");
        assertThat(userRepository.findByEmail("newuser2@newuser.com")).isEmpty();
    }

    @Test
    @DisplayName("Проверка поиска пользователя по email")
    void findByEmail() {
        assertThat(userRepository.findByEmail("slav@slav.com")).isEqualTo(Optional.of(user));
    }

    @Test
    @DisplayName("Проверка поиска пользователя по имени")
    void findByName() {
        assertThat(userRepository.findByName("slav")).isEqualTo(Optional.of(user));
    }

    @Test
    @DisplayName("Проверка поиска пользователя по ID")
    void findById() {
        assertThat(userRepository.findById(2L)).isEqualTo(Optional.of(user));
    }

    @Test
    @DisplayName("Проверка существования пользователя по имени")
    void existsByName() {
        assertThat(userRepository.existsByName("slav")).isTrue();
    }

    @Test
    @DisplayName("Проверка существования пользователя по email")
    void existsByEmail() {
        assertThat(userRepository.existsByEmail("slav@slav.com")).isTrue();
    }

    @Test
    @DisplayName("Проверка получения всех пользователей")
    void findAll() {
        assertThat(userRepository.findAll().size()).isGreaterThan(1);
    }
}