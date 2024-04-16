package com.github.kagokla.store.model.cart;

import com.github.kagokla.store.model.TestModelFactory;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import javax.money.convert.MonetaryConversions;

import static org.assertj.core.api.Assertions.*;

class CartTest extends TestModelFactory {

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
    void shouldFailWhenCreatingCartWithInvalidCustomerId() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(IdGeneratorUtils.generateRandomId()));
    }

    @Test
    void shouldSucceedWhenAddingLineItemToCart() {
        final var cart = buildRandomCart();

        final var firstProduct = buildRandomProductWithPriceUSD();
        cart.addLineItemOrIncreaseLineItem(firstProduct, firstProduct.stock());
        final var secondProduct = buildRandomProductWithPriceUSD();
        cart.addLineItemOrIncreaseLineItem(secondProduct, secondProduct.stock());

        assertThat(cart.getLineItems()).hasSize(2);
        assertThat(cart.getLineItems().getFirst())
                .isNotEqualTo(cart.getLineItems().getLast());
        assertThat(cart.getLineItems().getFirst().quantity()).isEqualTo(firstProduct.stock());
        assertThat(cart.getLineItems().getLast().quantity()).isEqualTo(secondProduct.stock());
    }

    @Test
    void shouldSucceedWhenIncreasingLineItemQuantityInCart() {
        final var cart = buildRandomCart();

        final var bookProduct = buildPEAABook();
        cart.addLineItemOrIncreaseLineItem(bookProduct, 5);
        cart.addLineItemOrIncreaseLineItem(bookProduct, 2);

        assertThat(cart.getLineItems()).hasSize(1);
        assertThat(cart.getLineItems().getFirst().quantity()).isEqualTo(7);
    }

    @Test
    void shouldFailWhenAddingMoreLineItemAlthoughCartIsFull() {
        final var cart = buildRandomCart();

        for (int i = 0; i < Cart.MAX_CART_ITEMS; i++) {
            final var product = buildRandomProductWithPriceUSD();
            cart.addLineItemOrIncreaseLineItem(product, product.stock());
        }
        assertThat(cart.getLineItems()).hasSize(Cart.MAX_CART_ITEMS);

        final var bookProduct = buildPEAABook();
        assertThatExceptionOfType(MaxCartItemsReachedException.class)
                .isThrownBy(() -> cart.addLineItemOrIncreaseLineItem(bookProduct, 0));
        assertThat(cart.getLineItems()).hasSize(Cart.MAX_CART_ITEMS);
    }

    @Test
    void shouldFailWhenAddingInvalidProductToCart() {
        final var cart = buildRandomCart();

        assertThatIllegalArgumentException().isThrownBy(() -> cart.addLineItemOrIncreaseLineItem(null, 0));
    }

    @Test
    void shouldFailWhenAddingInvalidQuantityToCart() {
        final var cart = buildRandomCart();
        final var product = buildRandomProductWithPriceUSD();

        assertThatIllegalArgumentException().isThrownBy(() -> cart.addLineItemOrIncreaseLineItem(product, -5));
    }

    @Test
    void shouldSucceedWhenUpdatingLineItemAlthoughCartIsFull() {
        final var cart = buildRandomCart();
        final var bookProduct = buildPEAABook();
        cart.addLineItemOrIncreaseLineItem(bookProduct, 2);

        for (int i = 1; i < Cart.MAX_CART_ITEMS; i++) {
            final var product = buildRandomProductWithPriceUSD();
            cart.addLineItemOrIncreaseLineItem(product, product.stock());
        }
        assertThat(cart.getLineItems()).hasSize(Cart.MAX_CART_ITEMS);

        cart.addLineItemOrIncreaseLineItem(bookProduct, 8);
        assertThat(cart.getLineItems().getFirst().quantity()).isEqualTo(10);
        assertThat(cart.getLineItems()).hasSize(Cart.MAX_CART_ITEMS);
    }

    @Test
    void shouldSucceedWhenReplacingLineItemInCart() {
        final var cart = buildRandomCart();
        final var bookProduct = buildPEAABook();

        cart.addLineItemOrIncreaseLineItem(bookProduct, 22);
        assertThat(cart.getLineItems()).hasSize(1);
        assertThat(cart.getLineItems().getFirst().quantity()).isEqualTo(22);

        cart.replaceLineItem(bookProduct, 11);
        assertThat(cart.getLineItems()).hasSize(1);
        assertThat(cart.getLineItems().getFirst().quantity()).isEqualTo(11);
    }

    @Test
    void shouldFailWhenReplacingUnknownLineItemInCart() {
        final var cart = buildRandomCart();
        final var product = buildRandomProductWithPriceUSD();

        assertThatExceptionOfType(UnknownCartItemException.class).isThrownBy(() -> cart.replaceLineItem(product, 6));
    }

    @Test
    void shouldSucceedWhenChangingCartCurrency() {
        final var cart = buildRandomCart();

        assertThat(cart.currency()).isEqualByComparingTo(Monetary.getCurrency("EUR"));

        final var usdCurrency = Monetary.getCurrency("USD");
        cart.changeCurrency(usdCurrency);

        assertThat(cart.currency()).isEqualByComparingTo(usdCurrency);
    }

    @Test
    void shouldFailWhenChangingCartCurrencyWithMissingCurrency() {
        final var cart = buildRandomCart();

        assertThatIllegalArgumentException().isThrownBy(() -> cart.changeCurrency(null));
    }

    @Test
    void shouldSucceedWhenGettingCartTotal() {
        final var cart = buildRandomCart();
        final var currencyConversion = MonetaryConversions.getConversion(cart.currency());

        final var firstProduct = buildRandomProductWithPriceUSD();
        final var firstProductQuantity = firstProduct.stock();
        final var firstProductSubTotal = firstProduct
                .price()
                .with(currencyConversion)
                .with(Monetary.getDefaultRounding())
                .multiply(firstProductQuantity);
        cart.addLineItemOrIncreaseLineItem(firstProduct, firstProductQuantity);
        final var secondProduct = buildRandomProductWithPriceUSD();
        final var secondProductQuantity = secondProduct.stock();
        final var secondProductSubTotal = secondProduct
                .price()
                .with(currencyConversion)
                .with(Monetary.getDefaultRounding())
                .multiply(secondProductQuantity);
        cart.addLineItemOrIncreaseLineItem(secondProduct, secondProductQuantity);
        final var thirdProduct = buildRandomProductWithPriceUSD();
        final var thirdProductQuantity = thirdProduct.stock();
        final var thirdProductSubTotal = thirdProduct
                .price()
                .with(currencyConversion)
                .with(Monetary.getDefaultRounding())
                .multiply(thirdProductQuantity);
        cart.addLineItemOrIncreaseLineItem(thirdProduct, thirdProductQuantity);

        assertThat(cart.subTotal())
                .isEqualTo(firstProductSubTotal.add(secondProductSubTotal).add(thirdProductSubTotal));
    }

    @Test
    void shouldSucceedWhenGettingEmptyCartTotal() {
        final var cart = buildRandomCart();

        assertThat(cart.subTotal()).isEqualTo(Money.zero(Monetary.getCurrency("EUR")));
    }

    @Test
    void shouldReturnStringRepresentation() {
        final var cart = buildRandomCart();

        assertThat(cart).asString().startsWith("Cart");
    }

    private Cart buildRandomCart() {
        return new Cart(IdGeneratorUtils.generateRandomCustomerId());
    }
}