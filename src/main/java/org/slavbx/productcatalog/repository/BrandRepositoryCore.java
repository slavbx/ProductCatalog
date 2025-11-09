package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.Brand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация репозитория для хранения брендов {@link BrandRepository}
 */
public class BrandRepositoryCore implements BrandRepository {
    private Long lastId;
    private final Map<Long, Brand> brands;

    public BrandRepositoryCore() {
        this.brands = new HashMap<>();
        this.lastId = 0L;
    }

    @Override
    public Brand save(Brand brand) {
        if (brand.getId() == null) {
            brand.setId(++lastId);
        }
        brands.put(brand.getId(), brand);
        return brand;
    }

    @Override
    public void deleteByName(String name) {
        Long id = null;
        for (Brand brand: brands.values()) {
            if (brand.getName().equals(name)) {
                id = brand.getId();
                break;
            }
        }
        if (id != null) brands.remove(id);
    }

    @Override
    public Optional<Brand> findByName(String name) {
        for (Brand brand: brands.values()) {
            if (brand.getName().equals(name)) {
                return Optional.of(brand);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return Optional.ofNullable(brands.get(id));
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Brand> findAllBrands() {
        return brands.values().stream().toList();
    }
}
