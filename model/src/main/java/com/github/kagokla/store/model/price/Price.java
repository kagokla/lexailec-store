package com.github.kagokla.store.model.price;

import com.github.kagokla.store.model.utils.ValidatorUtils;

import java.math.BigDecimal;
import java.util.Currency;

public record Price(BigDecimal amount, Currency currency) {

    public Price {
        ValidatorUtils.requireNonNull(amount, "amount must not be null");
        ValidatorUtils.requireNonNegative(amount.signum(), "amount must not be negative");
        ValidatorUtils.requireNonNull(currency, "currency must not be null");

        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException("Scale of amount %s is greater than the scale of the currency %s".formatted(amount, currency));
        }
    }

    public Price add(final Price other) {
        ValidatorUtils.requireNonNull(other, "other must not be null");
        if (!currency.equals(other.currency())) {
            throw new IllegalArgumentException("Addition must be performed with same currencies");
        }

        return new Price(amount.add(other.amount), currency);
    }

    public Price multiply(final int quantity) {
        ValidatorUtils.requireNonNegative(quantity, "quantity must not be negative");

        return new Price(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }
}
