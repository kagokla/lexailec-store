package com.github.kagokla.store.model.price;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class PriceTest {

    @Test
    void shouldCreateNewPrice() {
        final var price = getPriceEUR("12.34");

        assertThat(price).isNotNull();
    }

    @Test
    void shouldFailWhenPriceAmountIsMissing() {
        assertThatNullPointerException().isThrownBy(() -> getPriceUSD(null));
    }

    @Test
    void shouldFailWhenPriceAmountIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> getPriceEUR("-2222"));
    }

    @Test
    void shouldFailWhenPriceCurrencyIsMissing() {
        assertThatNullPointerException().isThrownBy(() -> new Price(new BigDecimal("34.568"), null));
    }

    @Test
    void shouldFailWhenPriceCurrencyUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(new BigDecimal("666"), Currency.getInstance("KOKO")));
    }

    @Test
    void shouldFailWhenPriceCurrencyFractionDigitsGreaterThanAmountScale() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(new BigDecimal("98.7"), Currency.getInstance("JPY")));
    }

    @Test
    void shouldAddPrices() {
        final var firstPrice = getPriceEUR("9.09");
        final var secondPrice = getPriceEUR("102.34");

        final var result = firstPrice.add(secondPrice);
        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("111.43"));
        assertThat(result.currency()).isEqualTo(Currency.getInstance("EUR"));
    }

    @Test
    void shouldFailWhenAddingPricesWithDifferentCurrencies() {
        final var firstPrice = getPriceEUR("52.25");
        final var secondPrice = getPriceUSD("1.34");

        assertThatThrownBy(() -> firstPrice.add(secondPrice)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldFailWhenAddingPriceToNull() {
        final var price = getPriceUSD("123456789");

        assertThatNullPointerException().isThrownBy(() -> price.add(null));
    }

    @Test
    void shouldMultiplyPrices() {
        final var price = getPriceUSD("56.7");

        var result = price.multiply(3);
        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("170.1"));
        assertThat(result.currency()).isEqualTo(Currency.getInstance("USD"));

        result = price.multiply(0);
        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.currency()).isEqualTo(Currency.getInstance("USD"));
    }

    @Test
    void shouldFailWhenMultiplyingPriceWithNegativeQuantity() {
        final var price = getPriceEUR("145.6");

        assertThatThrownBy(() -> price.multiply(-20)).isInstanceOf(UnsupportedOperationException.class);
    }

    private Price getPriceEUR(final String expectedAmount) {
        return new Price(new BigDecimal(expectedAmount), Currency.getInstance("EUR"));
    }

    private Price getPriceUSD(final String expectedAmount) {
        return new Price(new BigDecimal(expectedAmount), Currency.getInstance("USD"));
    }

}