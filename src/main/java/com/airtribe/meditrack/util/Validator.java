package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;

public final class Validator {

    private Validator() {
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(fieldName + " must not be blank");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new InvalidDataException(fieldName + " must be positive");
        }
    }

    public static void requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new InvalidDataException(fieldName + " must not be negative");
        }
    }
}

