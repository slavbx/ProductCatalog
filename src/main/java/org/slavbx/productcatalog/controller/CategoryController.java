package org.slavbx.productcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "CategoryController", description = "API for working with categories")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ValidationUtil validationUtil;

    @GetMapping
    @Operation(summary = "Get all categories")
    @Auditable(action = "Получение всех категорий")
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        return categoryMapper.categoriesToCategoryDTOs(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    @Auditable(action = "Получение категории по id")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return categoryMapper.categoryToCategoryDTO(category);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get category by name")
    @Auditable(action = "Получение категории по name")
    public CategoryDTO getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return categoryMapper.categoryToCategoryDTO(category);
    }

    @PostMapping
    @Operation(summary = "Create category")
    @Auditable(action = "Создание категории")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        validationUtil.validate(categoryDTO);

        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        Category createdCategory = categoryService.create(category);
        CategoryDTO createdCategoryDTO = categoryMapper.categoryToCategoryDTO(createdCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoryDTO);
    }

    @PutMapping
    @Operation(summary = "Update category")
    @Auditable(action = "Сохранение категории")
    public CategoryDTO updateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        validationUtil.validate(categoryDTO);

        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        Category resultCategory = categoryService.save(category);
        return categoryMapper.categoryToCategoryDTO(resultCategory);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete category")
    @Auditable(action = "Удаление категории")
    public ResponseEntity<String> deleteCategory(@PathVariable String name) {
        categoryService.deleteByName(name);
        return ResponseEntity.ok("Successfully deleted");
    }
}
