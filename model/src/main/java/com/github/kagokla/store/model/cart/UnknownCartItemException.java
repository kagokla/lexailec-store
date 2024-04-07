package com.github.kagokla.store.model.cart;

public class UnknownCartItemException extends RuntimeException {

    public UnknownCartItemException(final String productId) {
        super("Product %s is missing in the cart".formatted(productId));
    }
}
