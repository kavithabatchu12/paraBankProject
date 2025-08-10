package com.parabank.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class TestUtils {
    public static String generateRandomUsername() {
        return "user_" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
    }
}
