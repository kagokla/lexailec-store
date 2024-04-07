package com.github.kagokla.store.model;

import com.github.kagokla.store.model.price.Price;
import com.github.kagokla.store.model.product.Product;
import org.apache.commons.lang3.RandomStringUtils;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public abstract class ModelTestBase {

    protected final SecureRandom random = new SecureRandom();

    protected Price buildPriceEUR(final BigDecimal expectedAmount) {
        return new Price(expectedAmount, Monetary.getCurrency("EUR"));
    }

    protected Price buildPriceUSD(final BigDecimal expectedAmount) {
        return new Price(expectedAmount, Monetary.getCurrency("USD"));
    }

    protected Product buildRandomProduct() {
        final var name = RandomStringUtils.randomAlphabetic(10);
        final var description = "Description for: " + name;
        final var price = buildPriceEUR(BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP));

        return new Product(name, description, price, random.nextInt(100));
    }

    protected Product buildPEAABook() {
        final var name = "Patterns of Enterprise Application Architecture";
        final var price = buildPriceEUR(new BigDecimal("45.81"));

        return new Product(name, null, price, 50);
    }
}
