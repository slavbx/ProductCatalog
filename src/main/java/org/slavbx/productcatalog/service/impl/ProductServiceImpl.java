package org.slavbx.productcatalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.ProductRepository;
import org.slavbx.productcatalog.service.ProductService;

import java.util.List;

/**
 * Реализация сервиса для управления продуктами.
 * Обеспечивает операции получения, создания, и поиска продуктов.
 */
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Product getProductById(Long id) throws NotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product getProductByName(String name) throws NotFoundException {
        return productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Product not found with name: " + name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> saveAll(List<Product> products) {
        products.forEach(this::save);
        return products;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product create(Product product) throws AlreadyExistsException {
        if (productRepository.existsByName(product.getName())) {
            throw new AlreadyExistsException("Product with name: " + product.getName() + " already exists");
        }
        return save(product);
    }

    @Override
    public void deleteByName(String name) throws NotFoundException {
        if (!productRepository.existsByName(name)) {
            throw new NotFoundException("Product with name: " + name + " not found");
        }
        productRepository.deleteByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAllProductsByUser(User user) {
        return productRepository.findAllProductsByUser(user);
    }
}
