package org.slavbx.productcatalog.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringJUnitConfig(TestContainerConfig.class)
@DisplayName("Тестирование BrandRepository")
class BrandRepositoryJdbcTest {
    @Autowired
    BrandRepository brandRepository;
    Brand brand = Brand.builder().name("Zalman").desc("Производитель систем охлаждения и корпусов").build();

    @Test
    @DisplayName("Проверка сохранения бренда")
    void save() {
        Brand newBrand1 = Brand.builder().name("newbrand1").desc("Производитель разного").build();
        assertThat(brandRepository.save(newBrand1)).isEqualTo(newBrand1);
    }

    @Test
    @DisplayName("Проверка удаления бренда по имени")
    void deleteByName() {
        brandRepository.save(Brand.builder().name("newbrand2").desc("Производитель разного").build());
        brandRepository.deleteByName("newBrand2");
        assertThat(brandRepository.findByName("newBrand2")).isEmpty();
    }

    @Test
    @DisplayName("Проверка поиска бренда по имени")
    void findByName() {
        assertThat(brandRepository.findByName("Zalman")).isEqualTo(Optional.of(brand));
    }

    @Test
    @DisplayName("Проверка поиска бренда по id")
    void findById() {
        assertThat(brandRepository.findById(1L)).isEqualTo(Optional.of(brand));
    }

    @Test
    @DisplayName("Проверка существования бренда по имени")
    void existsByName() {
        assertThat(brandRepository.existsByName("Zalman")).isTrue();
    }

    @Test
    @DisplayName("Проверка получения всех брендов")
    void findAllBrands() {
        assertThat(brandRepository.findAllBrands().size()).isGreaterThan(1);
    }
}