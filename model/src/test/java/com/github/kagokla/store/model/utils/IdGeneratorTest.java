package com.github.kagokla.store.model.utils;


import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class IdGeneratorTest {

    @Test
    void shouldSucceedWhenGenerateRandomId() {
        final var randomId = IdGenerator.generateRandomId();

        assertThat(randomId).isNotBlank();
    }

    @Test
    void shouldNotCollideWhenGenerateRandomIdCalled10000Times() {
        final var numberRandomIds = 100000;
        final var randomIds = Stream.generate(IdGenerator::generateRandomId).limit(numberRandomIds).collect(Collectors.toSet());

        assertThat(randomIds).hasSize(numberRandomIds);
    }

    @Test
    void shouldSucceedWhenGenerateRandomProductId() {
        final var randomProductId = IdGenerator.generateRandomProductId();

        assertThat(randomProductId).isNotBlank().startsWith(IdGenerator.PRODUCT_ID_PREFIX);
    }

    @Test
    void shouldSucceedWhenGenerateRandomCustomerId() {
        final var randomCustomerId = IdGenerator.generateRandomCustomerId();

        assertThat(randomCustomerId).isNotBlank().startsWith(IdGenerator.CUSTOMER_ID_PREFIX);
    }
}