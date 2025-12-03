package org.slavbx.productcatalog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация репозитория для хранения категорий {@link CategoryRepository}
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCore implements CategoryRepository {
    private Long lastId = 0L;
    private final Map<Long, Category> categories = new HashMap<>();

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(++lastId);
        }
        categories.put(category.getId(), category);
        return category;
    }

    @Override
    public void deleteByName(String name) {
        Long id = null;
        for (Category category: categories.values()) {
            if (category.getName().equals(name)) {
                id = category.getId();
                break;
            }
        }
        if (id != null) categories.remove(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        for (Category category: categories.values()) {
            if (category.getName().equals(name)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Category> findAllCategories() {
        return categories.values().stream().toList();
    }
}
