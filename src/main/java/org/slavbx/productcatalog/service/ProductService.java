package org.slavbx.productcatalog.service;

import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.model.User;

import java.util.List;

/**
 * Интерфейс для управления товарами.
 * Обеспечивает функционал для работы с продуктами, включая
 * получение по id и названию, сохранение, создание, удаление,
 * а также получение списка всех товаров.
 */
public interface ProductService {
    /**
     * Получение товара по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор продукта.
     * @return объект Product, соответствующий указанному id.
     * @throws NotFoundException если товар не найден
     */
    Product getProductById(Long id) throws NotFoundException;

    /**
     * Получение товара по его названию.
     *
     * @param name название продукта.
     * @return объект Product, соответствующий указанному названию.
     * @throws NotFoundException если товар с таким названием не найден
     */
    Product getProductByName(String name) throws NotFoundException;

    /**
     * Сохранение товара.
     *
     * @param product объект Product, который нужно сохранить.
     * @return сохранённый объект Product.
     */
    Product save(Product product);

    /**
     * Сохранение списка товаров.
     *
     * @param products объект List<Product>, который нужно сохранить.
     * @return сохранённый список Product.
     */
    List<Product> saveAll(List<Product> products);

    /**
     * Создание нового товара.
     *
     * @param product объект Product, представляющий новый товар.
     * @return объект Product, который был создан.
     * @throws AlreadyExistsException если товар с таким названием уже существует
     */
    Product create(Product product) throws AlreadyExistsException;

    /**
     * Удаление товара по названию.
     *
     * @param name название товара для удаления.
     * @throws NotFoundException если товар с таким названием не найден.
     */
    void deleteByName(String name) throws NotFoundException;

    /**
     * Получение списка всех товаров.
     *
     * @return список всех зарегистрированных товаров.
     */
    List<Product> findAllProductsByUser(User user);
}
