package org.slavbx.productcatalog.service;

import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Category;

import java.util.List;

/**
 * Интерфейс для управления категориями товаров.
 * Обеспечивает функционал для работы с категориями, включая
 * получение категории по id и названию, сохранение и создание новых категорий,
 * а также получение списка всех категорий.
 */
public interface CategoryService {
    /**
     * Получение категории по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор категории.
     * @return объект Category, соответствующий указанному id.
     * @throws NotFoundException если категория не найдена
     */
    Category getCategoryById(Long id) throws NotFoundException;

    /**
     * Получение категории по её названию.
     *
     * @param name название категории.
     * @return объект Category, соответствующий указанному названию.
     * @throws NotFoundException если категория с таким названием не найдена
     */
    Category getCategoryByName(String name) throws NotFoundException;

    /**
     * Сохранение категории.
     *
     * @param category объект Category, который нужно сохранить.
     * @return сохранённый объект Category.
     */
    Category save(Category category);

    /**
     * Сохранение списка категорий.
     *
     * @param categories объект List<Category>, который нужно сохранить.
     * @return сохранённый список Category.
     */
    List<Category> saveAll(List<Category> categories);

    /**
     * Создание новой категории.
     *
     * @param category объект Category, представляющий новую категорию.
     * @return объект Category, который был создан.
     * @throws AlreadyExistsException если категория с таким названием уже существует
     */
    Category create(Category category) throws AlreadyExistsException;

    /**
     * Удаление категории по названию.
     *
     * @param name название категории для удаления.
     * @throws NotFoundException если категория с таким названием не найдена.
     */
    void deleteByName(String name) throws NotFoundException;

    /**
     * Получение списка всех категорий.
     *
     * @return список всех зарегистрированных категорий.
     */
    List<Category> findAllCategories();
}
