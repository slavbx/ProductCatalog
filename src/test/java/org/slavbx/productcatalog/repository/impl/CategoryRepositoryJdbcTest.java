package org.slavbx.productcatalog.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.repository.CategoryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование CategoryRepository")
class CategoryRepositoryJdbcTest {
    CategoryRepository categoryRepository = new CategoryRepositoryJdbc();
    Category category = Category.builder().name("Электроника").desc("Электронные устройства и аксессуары").build();

    @BeforeEach
    void setUp() {
        TestContainerConfig.getJdbcUrl();
    }

    @Test
    void save() {
        Category newCategory = Category.builder()
                .name("Gadgets")
                .desc("Гаджеты и аксессуары")
                .build();
        assertThat(categoryRepository.save(newCategory)).isEqualTo(newCategory);
    }

    @Test
    void deleteByName() {
        Category newCategory = Category.builder()
                .name("Temporary")
                .desc("Временная категория")
                .build();

        categoryRepository.save(Category.builder().name("Temporary").desc("Временная категория").build());
        categoryRepository.deleteByName("Temporary");
        assertThat(categoryRepository.findByName("Temporary")).isEmpty();
    }

    @Test
    void findByName() {
        assertThat(categoryRepository.findByName("Электроника")).isEqualTo(Optional.of(category));
    }

    @Test
    void findById() {
        assertThat(categoryRepository.findById(1L)).isEqualTo(Optional.of(category));
    }

    @Test
    void existsByName() {
        assertThat(categoryRepository.existsByName("Электроника")).isTrue();
    }

    @Test
    void findAllCategories() {
        assertThat(categoryRepository.findAllCategories().size()).isGreaterThan(1);
    }
}