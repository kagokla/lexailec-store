package com.github.kagokla.store.model.product;

import com.github.kagokla.store.model.ModelTestBase;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductTest extends ModelTestBase {

    private final SecureRandom random = new SecureRandom();

    @Test
    void shouldCreateNewProduct() {
        final var product = buildRandomProduct();

        assertProductIsValid(product);
    }

    @Test
    void shouldSucceedWhenSettingValidName() {
        final var product = buildRandomProduct();
        product.setName("shouldSucceedWhenSettingValidName");

        assertProductIsValid(product);
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
        assertProductIsValid(product);
        Assertions.assertThat(product.getDescription()).isNotBlank();


        product.setDescription(null);
        assertProductIsValid(product);
        Assertions.assertThat(product.getDescription()).isNull();
    }

    @Test
    void shouldSucceedWhenSettingValidPrice() {
        final var product = buildRandomProduct();
        final var price = buildPriceEUR(BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP));

        product.setPrice(price);

        assertProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidPrice() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.setPrice(null));
    }

    @Test
    void shouldSucceedWhenSettingValidRemainingStock() {
        final var product = buildRandomProduct();

        product.setRemainingStock(20);
        assertProductIsValid(product);
    }

    @Test
    void shouldFailWhenSettingInvalidRemainingStock() {
        final var product = buildRandomProduct();

        assertThatIllegalArgumentException().isThrownBy(() -> product.setRemainingStock(-5));
    }

    @Test
    void shouldFailWhenComparingProductsWithDifferentId() {
        final var firstProduct = buildRandomProduct();
        final var secondProduct = new Product(firstProduct.getName(), firstProduct.getDescription(), firstProduct.getPrice(), firstProduct.getRemainingStock());

        assertThat(firstProduct).isNotNull().isNotEqualTo(secondProduct);
    }

    @Test
    void shouldReturnStringRepresentation() {
        final var product = buildRandomProduct();

        assertThat(product).isNotNull().asString().startsWith("Product");
    }

    private void assertProductIsValid(final Product product) {
        assertThat(product).isNotNull();
        assertThat(product.getId()).startsWith(IdGeneratorUtils.PRODUCT_ID_PREFIX);
        assertThat(product.getName()).isNotBlank();
        assertThat(product.getPrice()).isNotNull();
        assertThat(product.getRemainingStock()).isPositive().isLessThan(100);
    }

    private Product buildRandomProduct() {
        final var name = RandomStringUtils.randomAlphabetic(10);
        final var description = "Description for: " + name;
        final var price = buildPriceUSD(BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP));

        return new Product(name, description, price, random.nextInt(100));
    }
}