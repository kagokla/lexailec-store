package com.github.kagokla.store.model.cart;


import com.github.kagokla.store.model.BaseEntity;
import com.github.kagokla.store.model.product.Product;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.Getter;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.convert.MonetaryConversions;

public class CartLineItem extends BaseEntity {

    private final Product product;
    @Getter
    private int quantity;

    public CartLineItem(final Product product, final int quantity) {
        super(IdGeneratorUtils.generateRandomCartLineItemId());

        ValidatorUtils.requireNonNull(product, "product");
        ValidatorUtils.requireNonNegative(quantity, "quantity");

        this.product = product;
        setQuantity(quantity);
    }

    public void increaseQuantity(final int augend) {
        ValidatorUtils.requireNonNegative(augend, "augend");

        setQuantity(this.quantity + augend);
    }

    public Money unitPrice(final CurrencyUnit currency) {
        ValidatorUtils.requireNonNull(currency, "currency");

        final var productPrice = product.getPrice();
        final var currencyConversion = MonetaryConversions.getConversion(currency);

        return productPrice.with(currencyConversion);
    }

    public Money subTotal(final CurrencyUnit currency) {
        final var unitPriceInCurrency = unitPrice(currency);
        return unitPriceInCurrency.multiply(quantity);
    }

    private void setQuantity(final int quantity) {
        if (product.getStock() < quantity) {
            throw new NotEnoughItemsInStockException(product.getStock(), product.getId(), quantity);
        }

        this.quantity = quantity;
    }

    public String toString() {
        return "CartLineItem(Id=" + id + ", productId=" + product.getId() + ", quantity=" + quantity + ")";
    }
}
