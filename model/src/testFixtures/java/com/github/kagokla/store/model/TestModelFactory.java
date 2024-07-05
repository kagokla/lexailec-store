package com.github.kagokla.store.model;

import com.github.kagokla.store.model.inventory.Product;
import org.apache.commons.lang3.RandomStringUtils;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public abstract class TestModelFactory {

    protected final SecureRandom random = new SecureRandom();

    protected Money buildPriceEUR(final BigDecimal expectedAmount) {
        return Money.of(expectedAmount, Monetary.getCurrency("EUR"));
    }

    protected Money buildPriceUSD(final BigDecimal expectedAmount) {
        return Money.of(expectedAmount, Monetary.getCurrency("USD"));
    }

    protected Product buildRandomProductWithPriceUSD() {
        final var name = RandomStringUtils.randomAlphabetic(10);
        final var description = "Description for: " + name;
        final var price = buildPriceUSD(BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP));

        return new Product(name, description, price, random.nextInt(100));
    }

    protected Product buildPEAABook() {
        final var name = "Patterns of Enterprise Application Architecture";
        final var price = buildPriceEUR(new BigDecimal("45.81"));

        return new Product(name, null, price, 50);
    }
}
