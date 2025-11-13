package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    /**
     * Сохраняет бренд
     * @param brand объект бренда для сохранения
     */
    Brand save(Brand brand);

    /**
     * Удаляет бренд по указанному названию
     * @param name название для удаления бренда
     */
    void deleteByName(String name);

    /**
     * Находит бренд по указанному name
     * @param name название для поиска бренда
     * @return объект Optional, содержащий найденный бренд, или пустой объект, если бренд не найден
     */
    Optional<Brand> findByName(String name);

    /**
     * Находит бренд по id
     * @param id идентификатор для поиска бренда
     * @return объект Optional, содержащий найденный бренд, или пустой объект, если бренд не найден
     */
    Optional<Brand> findById(Long id);

    /**
     * Существует ли бренд под именем
     * @param name идентификатор для поиска бренда
     * @return boolean, означающий существование бренда
     */
    boolean existsByName(String name);

    /**
     * Находит все бренды
     * @return список брендов
     */
    List<Brand> findAllBrands();
}
