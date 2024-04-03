package com.github.kagokla.store.model.utils;

import java.security.SecureRandom;
import java.util.Base64;

public final class IdGenerator {
    public static final String PRODUCT_ID_PREFIX = "prd";
    public static final String CUSTOMER_ID_PREFIX = "cst";
    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    private IdGenerator() {
    }

    public static String generateRandomId() {
        final var buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }

    public static String generateRandomProductId() {
        return PRODUCT_ID_PREFIX + generateRandomId();
    }

    public static String generateRandomCustomerId() {
        return CUSTOMER_ID_PREFIX + generateRandomId();
    }
}
