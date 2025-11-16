package org.slavbx.productcatalog.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.repository.BrandRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование BrandRepository")
class BrandRepositoryJdbcTest {
    BrandRepository brandRepository = new BrandRepositoryJdbc();
    Brand brand = Brand.builder().name("Zalman").desc("Производитель систем охлаждения и корпусов").build();

    @BeforeEach
    void setUp() {
        TestContainerConfig.getJdbcUrl();
    }

    @Test
    void save() {
        Brand newBrand1 = Brand.builder().name("newbrand1").desc("Производитель разного").build();
        assertThat(brandRepository.save(newBrand1)).isEqualTo(newBrand1);
    }

    @Test
    void deleteByName() {
        brandRepository.save(Brand.builder().name("newbrand2").desc("Производитель разного").build());
        brandRepository.deleteByName("newBrand2");
        assertThat(brandRepository.findByName("newBrand2")).isEmpty();
    }

    @Test
    void findByName() {
        assertThat(brandRepository.findByName("Zalman")).isEqualTo(Optional.of(brand));
    }

    @Test
    void findById() {
        assertThat(brandRepository.findById(1L)).isEqualTo(Optional.of(brand));
    }

    @Test
    void existsByName() {
        assertThat(brandRepository.existsByName("Zalman")).isTrue();
    }

    @Test
    void findAllBrands() {
        assertThat(brandRepository.findAllBrands().size()).isGreaterThan(1);
    }
}