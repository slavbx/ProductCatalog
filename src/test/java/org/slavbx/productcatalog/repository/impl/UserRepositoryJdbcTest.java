package org.slavbx.productcatalog.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.Level;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование UserRepository")
class UserRepositoryJdbcTest {
    UserRepository userRepository = new UserRepositoryJdbc();
    User user = User.builder().email("slav@slav.com").level(Level.USER).name("slav").password("slav").build();

    @BeforeEach
    void setUp() {
        TestContainerConfig.getJdbcUrl();
    }

    @Test
    void save() {
        User newUser1 = User.builder().email("newuser1@newuser.com").level(Level.USER).name("newuser1").password("newuser1").build();
        assertThat(userRepository.save(newUser1)).isEqualTo(newUser1);
    }

    @Test
    void deleteByEmail() {
        User newUser2 = User.builder().email("newuser2@newuser.com").level(Level.USER).name("newuser2").password("newuser2").build();
        userRepository.save(newUser2);
        userRepository.deleteByEmail("newuser2@newuser.com");
        assertThat(userRepository.findByEmail("newuser2@newuser.com")).isEmpty();
    }

    @Test
    void findByEmail() {
        assertThat(userRepository.findByEmail("slav@slav.com")).isEqualTo(Optional.of(user));
    }

    @Test
    void findByName() {
        assertThat(userRepository.findByName("slav")).isEqualTo(Optional.of(user));
    }

    @Test
    void findById() {
        assertThat(userRepository.findById(2L)).isEqualTo(Optional.of(user));
    }

    @Test
    void existsByName() {
        assertThat(userRepository.existsByName("slav")).isTrue();
    }

    @Test
    void existsByEmail() {
        assertThat(userRepository.existsByEmail("slav@slav.com")).isTrue();
    }

    @Test
    void findAllUsers() {
        assertThat(userRepository.findAllUsers().size()).isGreaterThan(1);
    }
}