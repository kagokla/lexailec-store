package com.github.kagokla.store.model.price;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Price(BigDecimal amount, Currency currency) {

    public Price {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }

        Objects.requireNonNull(currency, "currency must not be null");
        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException("Scale of amount %s is greater than the scale of the currency %s".formatted(amount, currency));
        }
    }

    public Price add(final Price other) {
        Objects.requireNonNull(other, "other must not be null");

        if (!currency.equals(other.currency())) {
            throw new UnsupportedOperationException("Addition must be performed with same currencies");
        }
        return new Price(amount.add(other.amount), currency);
    }

    public Price multiply(final int quantity) {
        if (quantity < 0) {
            throw new UnsupportedOperationException("Multiplication must be performed with a positive quantity");
        }
        return new Price(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }
}
