package com.smoothstack.jan2020.LmsJDBC.ui;

import java.util.Objects;
import java.util.Scanner;

public class KeyboardScanner {

    private static final Scanner keyboardScanner = new Scanner(System.in);

    public static Scanner getInstance() {
        return keyboardScanner;
    }

    public static String getStringOrDefault(String prompt, String defaultValue) {
        Objects.requireNonNull(defaultValue);

        System.out.printf(" [ %s ] ", defaultValue);
        String input = getString(prompt);
        if ("".equals(input))
            return defaultValue;
        else
            return input;
    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        return keyboardScanner.nextLine().trim();
    }

    public static int getIntegerOrDefault(String prompt, int defaultValue) {
        return getIntegerOrDefault(prompt, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    public static int getIntegerOrDefault(String prompt, int defaultValue, int min, int max) {
        while(true)
            try {
                int input = Integer.parseInt(getStringOrDefault(prompt, String.valueOf(defaultValue)));
                if ((input < min) || (input > max))
                    throw new NumberFormatException();
                return input;
            } catch (NumberFormatException e) {
                System.out.printf(" *** Please enter a number between %d and %d.\n\n", min, max);
            }
    }

    public static int getInteger(String prompt) {
        return getInteger(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int getInteger(String prompt, int min, int max) {
        while(true)
            try {
                int input = Integer.parseInt(getString(prompt));
                if ((input < min) || (input > max))
                   throw new NumberFormatException();
                return input;
            } catch (NumberFormatException e) {
                System.out.printf(" *** Please enter a number between %d and %d.\n\n", min, max);
            }
    }
}
