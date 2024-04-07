package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.ModelTestBase;
import com.github.kagokla.store.model.price.Price;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class CartLineItemTest extends ModelTestBase {

    @Test
    void shouldCreateNewCartLineItem() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThat(cartLineItem).isNotNull();
        assertThat(cartLineItem.getId()).startsWith(IdGeneratorUtils.CART_LINE_ITEM_ID_PREFIX);
        assertThat(cartLineItem.getQuantity()).isPositive();
    }

    @Test
    void shouldFailWhenProductIsMissing() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CartLineItem(null, 20));
    }

    @Test
    void shouldFailWhenItemQuantityIsNegative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new CartLineItem(buildRandomProduct(), -6));
    }

    @Test
    void shouldFailWhenItemQuantityGreaterThanStock() {
        final var product = buildRandomProduct();
        final var quantity = product.getStock() * 10;

        assertThatExceptionOfType(NotEnoughItemsInStockException.class).isThrownBy(() -> new CartLineItem(product, quantity));
    }

    @Test
    void shouldSucceedWhenGettingUnitPrice() {
        final var cartLineItem = buildDefaultCartLineItem();

        final var expectedPrice = new Price(BigDecimal.ONE, Monetary.getCurrency("JPY"));
        final var unitPrice = cartLineItem.unitPrice(Monetary.getCurrency("JPY"));

        assertThat(unitPrice).isNotNull().isEqualTo(expectedPrice);
    }

    @Test
    void shouldFailWhenGettingUnitPriceWithCurrencyNull() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThatIllegalArgumentException().isThrownBy(() -> cartLineItem.unitPrice(null));
    }

    @Test
    void shouldSucceedWhenGettingSubTotal() {
        final var cartLineItem = buildDefaultCartLineItem();

        var expectedPrice = new Price(BigDecimal.ONE, Monetary.getCurrency("USD"));
        var subTotal = cartLineItem.subTotal(Monetary.getCurrency("USD"));
        assertThat(subTotal).isNotNull().isEqualTo(expectedPrice);

        expectedPrice = new Price(BigDecimal.ONE, Monetary.getCurrency("XOF"));
        subTotal = cartLineItem.subTotal(Monetary.getCurrency("XOF"));
        assertThat(subTotal).isNotNull().isEqualTo(expectedPrice);
    }

    @Test
    void shouldFailWhenGettingSubTotalWithCurrencyNull() {
        final var cartLineItem = buildDefaultCartLineItem();

        assertThatIllegalArgumentException().isThrownBy(() -> cartLineItem.subTotal(null));
    }

    @Test
    void shouldSucceedWhenIncreasingItemQuantity() {
        final var cartLineItem = buildDefaultCartLineItem();

        cartLineItem.increaseQuantity(15);

        assertThat(cartLineItem.getQuantity()).isEqualTo(20);
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

        assertThat(cartLineItem).isNotNull().asString().startsWith("CartLineItem");
    }

    private CartLineItem buildDefaultCartLineItem() {
        return new CartLineItem(buildPEAABook(), 5);
    }
}