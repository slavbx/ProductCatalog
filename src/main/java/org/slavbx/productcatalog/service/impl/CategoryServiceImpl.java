package org.slavbx.productcatalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.repository.CategoryRepository;
import org.slavbx.productcatalog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления категориями.
 * Предоставляет функционал для работы с категориями, включая
 * получение их по id и email, создание, удаление
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getCategoryById(Long id) throws NotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getCategoryByName(String name) throws NotFoundException {
        return categoryRepository.findByName(name).orElseThrow(() -> new NotFoundException("Category not found with name: " + name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> saveAll(List<Category> categories) {
        categories.forEach(this::save);
        return categories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category create(Category category) throws AlreadyExistsException {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException("Category with name: " + category.getName() + " already exists");
        }
        return save(category);
    }

    @Override
    public void deleteByName(String name) throws NotFoundException {
        if (!categoryRepository.existsByName(name)) {
            throw new AlreadyExistsException("Category with name: " + name + " not found");
        }
        categoryRepository.deleteByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAllCategories();
    }
}
