package com.github.kagokla.store.model.cart;

public class NotEnoughItemsInStockException extends RuntimeException {

    public NotEnoughItemsInStockException(final int stock, final String productId, final int quantity) {
        super("Stock (%d) of product %s is less than the requested total quantity (%d)"
                .formatted(stock, productId, quantity));
    }
}
