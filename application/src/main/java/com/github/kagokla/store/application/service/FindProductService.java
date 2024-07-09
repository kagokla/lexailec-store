package com.github.kagokla.store.application.service;

import com.github.kagokla.store.application.port.in.product.FindProductUseCase;
import com.github.kagokla.store.application.port.out.persistence.ProductRepository;
import com.github.kagokla.store.model.inventory.Product;
import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FindProductService implements FindProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findByNameOrDescription(final String query) {
        ValidatorUtils.requireNonNull(query, "query");
        if (query.length() < 2) {
            throw new IllegalArgumentException("query must be at least two characters long");
        }

        return productRepository.findByNameOrDescription(query);
    }
}
