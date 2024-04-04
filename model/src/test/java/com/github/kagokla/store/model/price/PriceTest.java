package com.github.kagokla.store.model.price;

import com.github.kagokla.store.model.ModelTestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PriceTest extends ModelTestBase {

    @Test
    void shouldCreateNewPrice() {
        final var price = buildPriceEUR(new BigDecimal("12.34"));

        assertThat(price).isNotNull();
    }

    @Test
    void shouldFailWhenPriceAmountIsMissing() {
        assertThatIllegalArgumentException().isThrownBy(() -> buildPriceUSD(null));
    }

    @Test
    void shouldFailWhenPriceAmountIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> buildPriceEUR(new BigDecimal("-2222")));
    }

    @Test
    void shouldFailWhenPriceCurrencyIsMissing() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Price(new BigDecimal("34.568"), null));
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
        final var firstPrice = buildPriceEUR(new BigDecimal("9.09"));
        final var secondPrice = buildPriceEUR(new BigDecimal("102.34"));

        final var result = firstPrice.add(secondPrice);
        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("111.43"));
        assertThat(result.currency()).isEqualTo(Currency.getInstance("EUR"));
    }

    @Test
    void shouldFailWhenAddingPricesWithDifferentCurrencies() {
        final var firstPrice = buildPriceEUR(new BigDecimal("52.25"));
        final var secondPrice = buildPriceUSD(new BigDecimal("1.34"));

        assertThatIllegalArgumentException().isThrownBy(() -> firstPrice.add(secondPrice));
    }

    @Test
    void shouldFailWhenAddingPriceToNull() {
        final var price = buildPriceUSD(new BigDecimal("123456789"));

        assertThatIllegalArgumentException().isThrownBy(() -> price.add(null));
    }

    @Test
    void shouldMultiplyPrices() {
        final var price = buildPriceUSD(new BigDecimal("56.7"));

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
        final var price = buildPriceEUR(new BigDecimal("145.6"));

        assertThatIllegalArgumentException().isThrownBy(() -> price.multiply(-20)).withMessageContaining("quantity must not be negative");
    }

}