package org.slavbx.productcatalog.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование CategoryRepository")
class CategoryRepositoryJdbcTest extends TestContainerConfig {
    @Autowired
    CategoryRepository categoryRepository;
    Category category = Category.builder().name("Electronics").desc("Электронные устройства и аксессуары").build();

    @Test
    @DisplayName("Проверка сохранения категории")
    void save() {
        Category newCategory = Category.builder()
                .name("Gadgets")
                .desc("Гаджеты и аксессуары")
                .build();
        assertThat(categoryRepository.save(newCategory)).isEqualTo(newCategory);
    }

    @Test
    @DisplayName("Проверка удаления категории по имени")
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
    @DisplayName("Проверка поиска категории по имени")
    void findByName() {
        assertThat(categoryRepository.findByName("Electronics")).isEqualTo(Optional.of(category));
    }

    @Test
    @DisplayName("Проверка поиска категории по ID")
    void findById() {
        assertThat(categoryRepository.findById(1L)).isEqualTo(Optional.of(category));
    }

    @Test
    @DisplayName("Проверка существования категории по имени")
    void existsByName() {
        assertThat(categoryRepository.existsByName("Electronics")).isTrue();
    }

    @Test
    @DisplayName("Проверка получения всех категорий")
    void findAllCategories() {
        assertThat(categoryRepository.findAllCategories().size()).isGreaterThan(1);
    }
}