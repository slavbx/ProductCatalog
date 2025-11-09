package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.model.User;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    /**
     * Сохраняет товар
     * @param product объект товара для сохранения
     */
    Product save(Product product);

    /**
     * Удаляет товар по указанному названию
     * @param name название для удаления товара
     */
    void deleteByName(String name);

    /**
     * Находит товар по указанному name
     * @param name название для поиска товара
     * @return объект Optional, содержащий найденный товар, или пустой объект, если товар не найден
     */
    Optional<Product> findByName(String name);

    /**
     * Находит товар по id
     * @param id идентификатор для поиска товара
     * @return объект Optional, содержащий найденный товар, или пустой объект, если товар не найден
     */
    Optional<Product> findById(Long id);

    /**
     * Существует ли товар под именем
     * @param name идентификатор для поиска товара
     * @return boolean, означающий существование товара
     */
    boolean existsByName(String name);

    /**
     * Находит всех товары пользователя
     * @return список товаров
     */
    List<Product> findAllProductsByUser(User user);
}
