package com.vrrom.util;

public class SanitizationUtils {
    public static String sanitizeCarMake(String make) {
        return make.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}
