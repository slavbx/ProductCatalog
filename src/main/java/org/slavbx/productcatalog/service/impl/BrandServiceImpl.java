package org.slavbx.productcatalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.repository.BrandRepository;
import org.slavbx.productcatalog.service.BrandService;

import java.util.List;

/**
 * Реализация сервиса для управления брендами.
 * Обеспечивает операции получения, создания, и поиска брендов.
 */
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand getBrandById(Long id) throws NotFoundException {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand getBrandByName(String name) throws NotFoundException {
        return brandRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Brand not found with name: " + name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brand> saveAll(List<Brand> brands) {
        brands.forEach(this::save);
        return brands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand create(Brand brand) throws AlreadyExistsException {
        if (brandRepository.existsByName(brand.getName())) {
            throw new AlreadyExistsException("Brand with name: " + brand.getName() + " already exists");
        }
        return save(brand);
    }

    @Override
    public void deleteByName(String name) throws NotFoundException {
        if (!brandRepository.existsByName(name)) {
            throw new AlreadyExistsException("Brand with name: " + name + " not found");
        }
        brandRepository.deleteByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brand> findAllBrands() {
        return brandRepository.findAllBrands();
    }
}
