package com.github.kagokla.store.model.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdGeneratorUtils {
    public static final String PRODUCT_ID_PREFIX = "prod_";
    public static final String CUSTOMER_ID_PREFIX = "cust_";
    public static final String CART_ID_PREFIX = "cart_";
    public static final String CART_LINE_ITEM_ID_PREFIX = "item_";
    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

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

    public static String generateRandomCartId() {
        return CART_ID_PREFIX + generateRandomId();
    }

    public static String generateRandomCartLineItemId() {
        return CART_LINE_ITEM_ID_PREFIX + generateRandomId();
    }
}
