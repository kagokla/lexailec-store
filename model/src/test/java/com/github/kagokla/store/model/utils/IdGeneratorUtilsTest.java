package com.github.kagokla.store.model.utils;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class IdGeneratorUtilsTest {

    @Test
    void shouldSucceedWhenGenerateRandomId() {
        final var randomId = IdGeneratorUtils.generateRandomId();

        assertThat(randomId).isNotBlank();
    }

    @Test
    void shouldNotCollideWhenGenerateRandomIdSeveralTimes() {
        final var numberRandomIds = 100000;
        final var randomIds = Stream.generate(IdGeneratorUtils::generateRandomId)
                .limit(numberRandomIds)
                .collect(Collectors.toSet());

        assertThat(randomIds).hasSize(numberRandomIds);
    }

    @Test
    void shouldSucceedWhenGenerateRandomProductId() {
        final var randomProductId = IdGeneratorUtils.generateRandomProductId();

        assertThatIdIsValid(randomProductId, IdGeneratorUtils.PRODUCT_ID_PREFIX);
    }

    @Test
    void shouldSucceedWhenGenerateRandomCustomerId() {
        final var randomCustomerId = IdGeneratorUtils.generateRandomCustomerId();

        assertThatIdIsValid(randomCustomerId, IdGeneratorUtils.CUSTOMER_ID_PREFIX);
    }

    @Test
    void shouldSucceedWhenGenerateRandomCartId() {
        final var randomCartId = IdGeneratorUtils.generateRandomCartId();

        assertThatIdIsValid(randomCartId, IdGeneratorUtils.CART_ID_PREFIX);
    }

    private void assertThatIdIsValid(final String randomId, final String randomIdPrefix) {
        assertThat(randomId).isNotBlank().startsWith(randomIdPrefix);
        assertThat(randomId.length()).isGreaterThan(randomIdPrefix.length());
    }
}