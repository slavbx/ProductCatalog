package org.slavbx.productcatalog.repository.impl;

import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация репозитория для хранения товаров {@link ProductRepository}
 */
public class ProductRepositoryCore implements ProductRepository {
    private Long lastId;
    private final Map<Long, Product> products = new HashMap<>();

    public ProductRepositoryCore() {
        this.lastId = 0L;
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(++lastId);
        }
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public void deleteByName(String name) {
        Long id = null;
        for (Product product: products.values()) {
            if (product.getName().equals(name)) {
                id = product.getId();
                break;
            }
        }
        if (id != null) products.remove(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        for (Product product: products.values()) {
            if (product.getName().equals(name)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    @Override
    public List<Product> findAllProductsByUser(User user) {
        return products.values().stream().filter(p -> p.getSeller().equals(user)).toList();
    }
}
