package com.github.kagokla.store.model.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtils {

    public static <T> void requireNonNull(T value, String message) {
        if (value == null)
            throw new IllegalArgumentException(message);
    }

    public static void requireNonBlank(final String value, String message) {
        if (StringUtils.isBlank(value))
            throw new IllegalArgumentException(message);
    }

    public static void requireNonNegative(final int value, String message) {
        if (value < 0)
            throw new IllegalArgumentException(message);
    }
}
