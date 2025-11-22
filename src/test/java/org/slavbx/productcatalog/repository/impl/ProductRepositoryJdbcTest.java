package org.slavbx.productcatalog.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.*;
import org.slavbx.productcatalog.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование ProductRepository")
class ProductRepositoryJdbcTest {
    ProductRepository productRepository = new ProductRepositoryJdbc();
    User user = User.builder()
            .id(2L)
            .email("slav@slav.com")
            .level(Level.USER)
            .name("slav")
            .password("slav")
            .build();
    Brand brand = Brand.builder()
            .id(1L)
            .name("Zalman")
            .desc("Производитель систем охлаждения и корпусов")
            .build();
    Category category = Category.builder()
            .id(1L)
            .name("Электроника")
            .desc("Электронные устройства и аксессуары")
            .build();
    Product product = Product.builder()
            .id(1L)
            .name("Корпус ПК")
            .brand(brand)
            .desc("Прочный и стильный корпус для сборки ПК")
            .category(category)
            .price(new BigDecimal("3200.00"))
            .quantity(12)
            .seller(user)
            .createDate(LocalDate.now())
            .build();


    @BeforeEach
    void setUp() {
        TestContainerConfig.getJdbcUrl();
    }

    @Test
    void save() {
        Product newproduct = productRepository.findByName("Накопитель").get();
        productRepository.deleteByName("Накопитель");
        assertThat(productRepository.existsByName("Накопитель")).isFalse();
        productRepository.save(newproduct);
        assertThat(productRepository.findByName("Накопитель")).isEqualTo(Optional.of(newproduct));
    }

    @Test
    void deleteByName() {
        assertThat(productRepository.existsByName("Аккумулятор")).isTrue();
        productRepository.deleteByName("Аккумулятор");
        assertThat(productRepository.existsByName("Аккумулятор")).isFalse();
    }

    @Test
    void findByName() {
        assertThat(productRepository.findByName("Корпус ПК")).isEqualTo(Optional.of(product));
    }

    @Test
    void findById() {
        assertThat(productRepository.findById(1L)).isEqualTo(Optional.of(product));
    }

    @Test
    void existsByName() {
        assertThat(productRepository.existsByName("Корпус ПК")).isTrue();
    }

    @Test
    void findAllProductsByUser() {
        assertThat(productRepository.findAllProductsByUser(user).size()).isGreaterThan(1);
    }
}