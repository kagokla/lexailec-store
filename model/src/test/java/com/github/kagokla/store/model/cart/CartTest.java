package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.ModelTestBase;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CartTest extends ModelTestBase {

    @Test
    void shouldCreateNewCart() {
        final var cart = buildRandomCart();

        assertThat(cart).isNotNull();
        assertThat(cart.id()).startsWith(IdGeneratorUtils.CART_ID_PREFIX);
        assertThat(cart.currency()).isEqualByComparingTo(Monetary.getCurrency("EUR"));
    }

    @Test
    void shouldFailWhenCreatingCartWithMissingCustomerId() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(null));
    }

    @Test
    void shouldFailWhenCreatingCartWithEmptyCustomerId() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(""));
    }

    @Test
    void shouldSucceedWhenChangingCartCurrency() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(""));
    }

    @Test
    void shouldSucceedWhenAddingItemToCart() throws NotEnoughItemsInStockException, MaxCartItemsReachedException {
        final var cart = buildRandomCart();

        cart.addLineItemOrIncreaseLineItem(buildRandomProduct(), 5);
        cart.addLineItemOrIncreaseLineItem(buildRandomProduct(), 2);

        assertThat(cart).isNotNull();
        assertThat(cart.getLineItems()).isEqualTo(List.of(5, 2));
    }

    @Test
    void shouldReturnStringRepresentation() {
        final var cart = buildRandomCart();

        assertThat(cart).isNotNull().asString().startsWith("Cart");
    }

    private Cart buildRandomCart() {
        return new Cart(IdGeneratorUtils.generateRandomCustomerId());
    }
}