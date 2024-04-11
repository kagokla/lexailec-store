package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.ModelTestBase;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import static org.assertj.core.api.Assertions.*;

class CartLineItemTest extends ModelTestBase {

    @Test
    void shouldCreateNewCartLineItem() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThat(cartLineItem).isNotNull();
        assertThat(cartLineItem.quantity()).isPositive();
    }

    @Test
    void shouldFailWhenProductIsMissing() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CartLineItem(null, 20));
    }

    @Test
    void shouldFailWhenItemQuantityIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CartLineItem(buildRandomProductWithPriceUSD(), -6));
    }

    @Test
    void shouldFailWhenItemQuantityGreaterThanStock() {
        final var product = buildRandomProductWithPriceUSD();
        final var quantity = product.stock() * 10;

        assertThatExceptionOfType(NotEnoughItemsInStockException.class).isThrownBy(() -> new CartLineItem(product, quantity));
    }

    @Test
    void shouldSucceedWhenGettingUnitPrice() {
        final var cartLineItem = buildDefaultCartLineItem();

        final var unitPrice = cartLineItem.unitPrice(Monetary.getCurrency("JPY"));

        assertThat(unitPrice).isNotNull();
        assertThat(unitPrice.getCurrency()).isEqualTo(Monetary.getCurrency("JPY"));
    }

    @Test
    void shouldFailWhenGettingUnitPriceWithCurrencyNull() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThatIllegalArgumentException().isThrownBy(() -> cartLineItem.unitPrice(null));
    }

    @Test
    void shouldSucceedWhenGettingSubTotal() {
        final var cartLineItem = buildDefaultCartLineItem();

        var subTotal = cartLineItem.subTotal(Monetary.getCurrency("USD"));
        assertThat(subTotal).isNotNull();
        assertThat(subTotal.getCurrency()).isEqualTo(Monetary.getCurrency("USD"));

        subTotal = cartLineItem.subTotal(Monetary.getCurrency("THB"));
        assertThat(subTotal).isNotNull();
        assertThat(subTotal.getCurrency()).isEqualTo(Monetary.getCurrency("THB"));
    }

    @Test
    void shouldFailWhenGettingSubTotalWithCurrencyNull() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThatIllegalArgumentException().isThrownBy(() -> cartLineItem.subTotal(null));
    }

    @Test
    void shouldSucceedWhenIncreasingItemQuantity() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThat(cartLineItem.increaseQuantity(15).quantity()).isEqualTo(20);
    }

    @Test
    void shouldFailWhenDecreasingItemQuantity() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThatIllegalArgumentException().isThrownBy(() -> cartLineItem.increaseQuantity(-5));
    }

    @Test
    void shouldFailWhenIncreasingItemQuantityMoreThanStock() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThatExceptionOfType(NotEnoughItemsInStockException.class).isThrownBy(() -> cartLineItem.increaseQuantity(1000));
    }

    @Test
    void shouldReturnStringRepresentation() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThat(cartLineItem).asString().startsWith("CartLineItem");
    }

    private CartLineItem buildDefaultCartLineItem() {
        return new CartLineItem(buildPEAABook(), 5);
    }
}