package com.github.kagokla.store.model.price;

import com.github.kagokla.store.model.utils.ValidatorUtils;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;

public record Price(BigDecimal amount, CurrencyUnit currency) {

    public Price {
        ValidatorUtils.requireNonNull(amount, "amount");
        ValidatorUtils.requireNonNegative(amount.signum(), "amount");
        ValidatorUtils.requireNonNull(currency, "currency");

        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException("Scale of amount %s is greater than the scale of the currency %s".formatted(amount, currency));
        }
    }

    public Price add(final Price other) {
        ValidatorUtils.requireNonNull(other, "other");
        if (!currency.equals(other.currency())) {
            throw new IllegalArgumentException("Addition must be performed with same currencies");
        }

        return new Price(amount.add(other.amount), currency);
    }

    public Price multiply(final int quantity) {
        ValidatorUtils.requireNonNegative(quantity, "quantity");

        return new Price(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }
}
