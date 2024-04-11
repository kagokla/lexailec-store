package com.github.kagokla.store.model.cart;


import com.github.kagokla.store.model.BaseEntity;
import com.github.kagokla.store.model.product.Product;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
public class Cart extends BaseEntity {

    private static final int MAX_CART_ITEMS = 10;
    @Getter
    private final String customerId;
    private final Map<String, Integer> cartItems = LinkedHashMap.newLinkedHashMap(MAX_CART_ITEMS);
    @Getter
    private CurrencyUnit currency;

    public Cart(final String customerId) {
        super(IdGeneratorUtils.generateRandomCartId());

        ValidatorUtils.requireNonBlank(customerId, "customer");
        if (!customerId.startsWith(IdGeneratorUtils.CUSTOMER_ID_PREFIX)) {
            throw new IllegalArgumentException("%s is not identified as an entity customer".formatted(customerId));
        }

        this.customerId = customerId;
        this.currency = Monetary.getCurrency("EUR");
    }

    public void addLineItemOrIncreaseLineItem(final Product product, final int quantity)
            throws MaxCartItemsReachedException, NotEnoughItemsInStockException {
        ValidatorUtils.requireNonNull(product, "product");
        ValidatorUtils.requireNonNegative(quantity, "quantity");

        if (cartItems.size() >= MAX_CART_ITEMS) {
            throw new MaxCartItemsReachedException(MAX_CART_ITEMS);
        }
        cartItems
                .putIfAbsent(product.id(), quantity);
    }

    public void replaceLineItem(final Product product, final int quantity)
            throws UnknownCartItemException, NotEnoughItemsInStockException {
        if (!cartItems.containsKey(product.id())) {
            throw new UnknownCartItemException(product.id());
        }
    }

    public List<Integer> getLineItems() {
        return List.copyOf(cartItems.values());
    }

    public void changeCurrency(final CurrencyUnit currency) {
        ValidatorUtils.requireNonNull(currency, "currency");

        this.currency = currency;
    }

    public String toString() {
        return "Cart(Id=" + id + ", customerId=" + customerId + ", currency=" + currency.getCurrencyCode() + ")";
    }

}
