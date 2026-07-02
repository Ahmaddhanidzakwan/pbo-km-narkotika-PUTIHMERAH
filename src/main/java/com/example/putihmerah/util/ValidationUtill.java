package com.example.putihmerah.util;

package com.example.putihmerah.util;

public class ValidationUtil {

    private ValidationUtil() {

    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Harus berupa angka bulat: \"" + s + "\"");
        }
    }

    public static int parseInt(String s, int min, int max) {
        int nilai = parseInt(s);

        if (nilai < min || nilai > max) {
            throw new IllegalArgumentException(
                    "Angka harus antara " + min + " dan " + max +
                            " (diberikan: " + nilai + ")");
        }

        return nilai;
    }