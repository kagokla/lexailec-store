package com.github.kagokla.store.model.product;

import com.github.kagokla.store.model.ModelTestBase;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductTest extends ModelTestBase {

    @Test
    void shouldCreateNewProduct() {
        final var product = buildRandomProduct();

        assertThatProductIsValid(product);
    }

    @Test
    void shouldSucceedWhenSettingValidName() {
        final var product = buildRandomProduct();
        product.name("shouldSucceedWhenSettingValidName");

        assertThatProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidName() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.name(null));
        assertThatIllegalArgumentException().isThrownBy(() -> product.name("        "));
    }

    @Test
    void shouldSucceedWhenSettingDescription() {
        final var product = buildRandomProduct();

        product.description("shouldSucceedWhenSettingDescription");
        assertThatProductIsValid(product);
        Assertions.assertThat(product.description()).isNotBlank();


        product.description(null);
        assertThatProductIsValid(product);
        Assertions.assertThat(product.description()).isNull();
    }

    @Test
    void shouldSucceedWhenSettingValidPrice() {
        final var product = buildRandomProduct();
        final var price = buildPriceEUR(BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP));

        product.price(price);

        assertThatProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidPrice() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.price(null));
    }

    @Test
    void shouldSucceedWhenSettingValidRemainingStock() {
        final var product = buildRandomProduct();

        product.stock(20);
        assertThatProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidRemainingStock() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.stock(-5));
    }

    @Test
    void shouldFailWhenComparingProductsWithDifferentId() {
        final var firstProduct = buildRandomProduct();
        final var secondProduct = new Product(firstProduct.name(), firstProduct.description(), firstProduct.price(), firstProduct.stock());

        assertThat(firstProduct).isNotNull().isNotEqualTo(secondProduct);
    }

    @Test
    void shouldReturnStringRepresentation() {
        final var product = buildRandomProduct();

        assertThat(product).isNotNull().asString().startsWith("Product");
    }

    private void assertThatProductIsValid(final Product product) {
        assertThat(product).isNotNull();
        assertThat(product.id()).startsWith(IdGeneratorUtils.PRODUCT_ID_PREFIX);
        assertThat(product.name()).isNotBlank();
        assertThat(product.price()).isNotNull();
        assertThat(product.stock()).isNotNegative().isLessThan(100);
    }
}