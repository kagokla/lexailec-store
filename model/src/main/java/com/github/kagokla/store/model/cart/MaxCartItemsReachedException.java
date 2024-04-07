package com.github.kagokla.store.model.cart;

public class MaxCartItemsReachedException extends RuntimeException {

    public MaxCartItemsReachedException(final int maxCartItems) {
        super("Maximum number of items (%s) in the cart has been reached".formatted(maxCartItems));
    }
}
