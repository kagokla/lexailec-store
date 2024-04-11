package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.product.Product;
import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.experimental.Accessors;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.convert.MonetaryConversions;

@Accessors(fluent = true)
public record CartLineItem(Product product, int quantity) {

    public CartLineItem {

        ValidatorUtils.requireNonNull(product, "product");
        ValidatorUtils.requireNonNegative(quantity, "quantity");

        if (product.stock() < quantity) {
            throw new NotEnoughItemsInStockException(product.stock(), product.id(), quantity);
        }
    }

    public CartLineItem increaseQuantity(final int augend) {
        ValidatorUtils.requireNonNegative(augend, "augend");

        return new CartLineItem(product, quantity + augend);
    }

    public Money unitPrice(final CurrencyUnit currency) {
        ValidatorUtils.requireNonNull(currency, "currency");

        final var productPrice = product.price();
        final var currencyConversion = MonetaryConversions.getConversion(currency);

        return productPrice.with(currencyConversion).with(Monetary.getDefaultRounding());
    }

    public Money subTotal(final CurrencyUnit currency) {
        final var unitPriceInCurrency = unitPrice(currency);
        return unitPriceInCurrency.multiply(quantity);
    }

    public String toString() {
        return "CartLineItem(productId=" + product.id() + ", quantity=" + quantity + ")";
    }
}
