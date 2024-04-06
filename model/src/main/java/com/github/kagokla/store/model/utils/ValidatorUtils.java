package com.github.kagokla.store.model.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtils {

    public static <T> void requireNonNull(T propertyValue, String propertyName) {
        if (null == propertyValue)
            throw new IllegalArgumentException("%s must not be null".formatted(propertyName));
    }

    public static void requireNonBlank(final String propertyValue, String propertyName) {
        if (StringUtils.isBlank(propertyValue))
            throw new IllegalArgumentException("%s must not be blank".formatted(propertyName));
    }

    public static void requireNonNegative(final int propertyValue, String propertyName) {
        if (propertyValue < 0)
            throw new IllegalArgumentException("%s must not be negative".formatted(propertyName));
    }
}
