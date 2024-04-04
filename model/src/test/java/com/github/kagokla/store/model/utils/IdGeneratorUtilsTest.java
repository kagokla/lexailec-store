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
        final var randomIds = Stream.generate(IdGeneratorUtils::generateRandomId).limit(numberRandomIds).collect(Collectors.toSet());

        assertThat(randomIds).hasSize(numberRandomIds);
    }

    @Test
    void shouldSucceedWhenGenerateRandomProductId() {
        final var randomProductId = IdGeneratorUtils.generateRandomProductId();

        assertThat(randomProductId).isNotBlank().startsWith(IdGeneratorUtils.PRODUCT_ID_PREFIX);
        assertThat(randomProductId.length()).isGreaterThan(IdGeneratorUtils.PRODUCT_ID_PREFIX.length());
    }

    @Test
    void shouldSucceedWhenGenerateRandomCustomerId() {
        final var randomCustomerId = IdGeneratorUtils.generateRandomCustomerId();

        assertThat(randomCustomerId).isNotBlank().startsWith(IdGeneratorUtils.CUSTOMER_ID_PREFIX);
        assertThat(randomCustomerId.length()).isGreaterThan(IdGeneratorUtils.CUSTOMER_ID_PREFIX.length());
    }
}