package com.github.kagokla.store.application.port.out.persistence;

import com.github.kagokla.store.model.inventory.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findByNameOrDescription(final String query);
}
