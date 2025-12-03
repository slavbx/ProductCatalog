package org.slavbx.productcatalog.controller;

import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.CategoryDTO;
import org.slavbx.productcatalog.mapper.CategoryMapper;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов к категориям.
 * Поддерживает получение, создание, обновление и удаление категорий.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CategoryController(CategoryService categoryService,
                              CategoryMapper categoryMapper,
                              ValidationUtil validationUtil) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    @Auditable(action = "Получение всех категорий")
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        return categoryMapper.categoriesToCategoryDTOs(categories);
    }

    @GetMapping("/{id}")
    @Auditable(action = "Получение категории по id")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return categoryMapper.categoryToCategoryDTO(category);
    }

    @GetMapping("/name/{name}")
    @Auditable(action = "Получение категории по name")
    public CategoryDTO getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return categoryMapper.categoryToCategoryDTO(category);
    }

    @PostMapping
    @Auditable(action = "Создание категории")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        validationUtil.validate(categoryDTO);

        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        Category createdCategory = categoryService.create(category);
        CategoryDTO createdCategoryDTO = categoryMapper.categoryToCategoryDTO(createdCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoryDTO);
    }

    @PutMapping
    @Auditable(action = "Сохранение категории")
    public CategoryDTO updateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        validationUtil.validate(categoryDTO);

        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        Category resultCategory = categoryService.save(category);
        return categoryMapper.categoryToCategoryDTO(resultCategory);
    }

    @DeleteMapping("/{name}")
    @Auditable(action = "Удаление категории")
    public ResponseEntity<String> deleteCategory(@PathVariable String name) {
        categoryService.deleteByName(name);
        return ResponseEntity.ok("Successfully deleted");
    }
}
