package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.BaseEntity;
import com.github.kagokla.store.model.inventory.Product;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
public class Cart extends BaseEntity {

    public static final int MAX_CART_ITEMS = 10;
    private final String customerId;
    private final Map<String, CartLineItem> cartLineItems = LinkedHashMap.newLinkedHashMap(MAX_CART_ITEMS);

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

    public void addLineItemOrIncreaseLineItem(final Product product, final int quantity) {
        ValidatorUtils.requireNonNull(product, "product");
        ValidatorUtils.requireNonNegative(quantity, "quantity");

        if (!cartLineItems.containsKey(product.id()) && MAX_CART_ITEMS == cartLineItems.size()) {
            throw new MaxCartItemsReachedException(MAX_CART_ITEMS);
        }

        cartLineItems.merge(
                product.id(),
                new CartLineItem(product, quantity),
                (oldValue, value) -> oldValue.increaseQuantity(quantity));
    }

    public void replaceLineItem(final Product product, final int quantity) {
        if (!cartLineItems.containsKey(product.id())) {
            throw new UnknownCartItemException(product.id());
        }

        cartLineItems.put(product.id(), new CartLineItem(product, quantity));
    }

    public Money subTotal() {
        return cartLineItems.values().stream()
                .map(item -> item.subTotal(currency))
                .reduce(Money.zero(currency), Money::add);
    }

    public List<CartLineItem> getLineItems() {
        return List.copyOf(cartLineItems.values());
    }

    public void changeCurrency(final CurrencyUnit currency) {
        ValidatorUtils.requireNonNull(currency, "currency");

        this.currency = currency;
    }

    public String toString() {
        return "Cart(Id=" + id + ", customerId=" + customerId + ", currency=" + currency.getCurrencyCode() + ")";
    }
}
