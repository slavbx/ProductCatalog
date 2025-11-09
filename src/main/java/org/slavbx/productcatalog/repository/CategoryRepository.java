package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    /**
     * Сохраняет категорию
     * @param category объект категории для сохранения
     */
    Category save(Category category);

    /**
     * Удаляет категорию по указанному названию
     * @param name название для удаления категории
     */
    void deleteByName(String name);

    /**
     * Находит категорию по указанному name
     * @param name название для поиска категории
     * @return объект Optional, содержащий найденную категорию, или пустой объект, если категория не найдена
     */
    Optional<Category> findByName(String name);

    /**
     * Находит категорию по id
     * @param id идентификатор для поиска категории
     * @return объект Optional, содержащий найденную категорию, или пустой объект, если категория не найдена
     */
    Optional<Category> findById(Long id);

    /**
     * Существует ли категория под именем
     * @param name идентификатор для поиска категории
     * @return boolean, означающий существование категории
     */
    boolean existsByName(String name);

    /**
     * Находит все категории
     * @return список категорий
     */
    List<Category> findAllCategories();
}
