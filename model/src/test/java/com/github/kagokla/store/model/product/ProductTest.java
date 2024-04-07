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
        product.setName("shouldSucceedWhenSettingValidName");

        assertThatProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidName() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.setName(null));
        assertThatIllegalArgumentException().isThrownBy(() -> product.setName("        "));
    }

    @Test
    void shouldSucceedWhenSettingDescription() {
        final var product = buildRandomProduct();

        product.setDescription("shouldSucceedWhenSettingDescription");
        assertThatProductIsValid(product);
        Assertions.assertThat(product.getDescription()).isNotBlank();


        product.setDescription(null);
        assertThatProductIsValid(product);
        Assertions.assertThat(product.getDescription()).isNull();
    }

    @Test
    void shouldSucceedWhenSettingValidPrice() {
        final var product = buildRandomProduct();
        final var price = buildPriceEUR(BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP));

        product.setPrice(price);

        assertThatProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidPrice() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.setPrice(null));
    }

    @Test
    void shouldSucceedWhenSettingValidRemainingStock() {
        final var product = buildRandomProduct();

        product.setStock(20);
        assertThatProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidRemainingStock() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.setStock(-5));
    }

    @Test
    void shouldFailWhenComparingProductsWithDifferentId() {
        final var firstProduct = buildRandomProduct();
        final var secondProduct = new Product(firstProduct.getName(), firstProduct.getDescription(), firstProduct.getPrice(), firstProduct.getStock());

        assertThat(firstProduct).isNotNull().isNotEqualTo(secondProduct);
    }

    @Test
    void shouldReturnStringRepresentation() {
        final var product = buildRandomProduct();

        assertThat(product).isNotNull().asString().startsWith("Product");
    }

    private void assertThatProductIsValid(final Product product) {
        assertThat(product).isNotNull();
        assertThat(product.getId()).startsWith(IdGeneratorUtils.PRODUCT_ID_PREFIX);
        assertThat(product.getName()).isNotBlank();
        assertThat(product.getPrice()).isNotNull();
        assertThat(product.getStock()).isPositive().isLessThan(100);
    }
}