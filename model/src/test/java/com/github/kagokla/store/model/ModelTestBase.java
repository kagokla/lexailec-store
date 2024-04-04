package com.github.kagokla.store.model;

import com.github.kagokla.store.model.price.Price;

import java.math.BigDecimal;
import java.util.Currency;

public abstract class ModelTestBase {

    protected Price buildPriceEUR(final BigDecimal expectedAmount) {
        return new Price(expectedAmount, Currency.getInstance("EUR"));
    }

    protected Price buildPriceUSD(final BigDecimal expectedAmount) {
        return new Price(expectedAmount, Currency.getInstance("USD"));
    }
}
