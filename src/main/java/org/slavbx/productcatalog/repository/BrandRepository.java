package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

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
     * Существует ли бренд под именем
     * @param name идентификатор для поиска бренда
     * @return boolean, означающий существование бренда
     */
    boolean existsByName(String name);
}
