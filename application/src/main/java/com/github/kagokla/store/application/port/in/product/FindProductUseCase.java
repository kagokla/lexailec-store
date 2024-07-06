package com.github.kagokla.store.application.port.in.product;

import com.github.kagokla.store.model.inventory.Product;

import java.util.List;

public interface FindProductUseCase {

    List<Product> findByNameOrDescription(final String query);
}
