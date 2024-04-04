package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CartTest {

    private Cart cart;

    @Test
    void shouldCreateNewCart() {
        final var cart = new Cart(IdGeneratorUtils.generateRandomCustomerId());

        assertThat(cart).isNotNull();
    }

    @Test
    void shouldFailWhenCustomerIdIsMissing() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(null));
    }

    @Test
    void shouldFailWhenCustomerIdIsEmpty() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(""));
    }
}