package org.slavbx.productcatalog.service;

import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Brand;

import java.util.List;

/**
 * Интерфейс для управления брендами товаров.
 * Обеспечивает функционал для работы с брендами, включая
 * получение бренда по id и названию, сохранение и создание новых брендов,
 * а также получение списка всех брендов.
 */
public interface BrandService {
    /**
     * Получение бренда по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор бренда.
     * @return объект Brand, соответствующий указанному id.
     * @throws NotFoundException если бренд не найден
     */
    Brand getBrandById(Long id) throws NotFoundException;

    /**
     * Получение бренда по названию.
     *
     * @param name название бренда.
     * @return объект Brand, соответствующий указанному названию.
     * @throws NotFoundException если бренд с таким названием не найден
     */
    Brand getBrandByName(String name) throws NotFoundException;

    /**
     * Сохранение бренда.
     *
     * @param brand объект Brand, который нужно сохранить.
     * @return сохранённый объект Brand.
     */
    Brand save(Brand brand);

    /**
     * Сохранение списка брендов.
     *
     * @param brands объект List<Brand>, который нужно сохранить.
     * @return сохранённый список Brand.
     */
    List<Brand> saveAll(List<Brand> brands);

    /**
     * Создание нового бренда.
     *
     * @param brand объект Brand, представляющий новый бренд.
     * @return созданный объект Brand.
     * @throws AlreadyExistsException если бренд с таким названием уже существует
     */
    Brand create(Brand brand) throws AlreadyExistsException;

    /**
     * Удаление бренда по названию.
     *
     * @param name название бренда для удаления.
     * @throws NotFoundException если бренд с таким названием не найден.
     */
    void deleteByName(String name) throws NotFoundException;

    /**
     * Получение списка всех брендов.
     *
     * @return список всех зарегистрированных брендов.
     */
    List<Brand> findAllBrands();
}
