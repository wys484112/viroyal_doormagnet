package com.viroyal.doormagnet.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private final static int MAX_INT = 2147483647;

    public static final String MDC_KEY = "invokeNo";

    public static String RandomCode(int n) {

        if (n < 1 || n > 10) {
            throw new IllegalArgumentException("Cannot random " + n + " bit number!");
        }

        Random r = new Random();
        if (n == 1) {
            return String.valueOf(r.nextInt(10));
        }

        int bitField = 0;
        char[] arr = new char[n];
        for (int i = 0; i < n; i++) {
            while (true) {
                int k = r.nextInt(10);
                if ((bitField & (1 << k)) == 0) {
                    bitField |= 1 << k;
                    arr[i] = (char) (k + '0');
                    break;
                }
            }
        }

        return new String(arr);
    }

    public static Integer getMySqlKey() {
        Long result;

        for (;;) {
            Integer n = (int) (Math.random() * 6 + 5);
            result = Long.valueOf(RandomCode(n));

            if (result <= MAX_INT)
                break;
        }

        return result.intValue();
    }

    public static String getMDCValue() {
        return UUID.randomUUID().toString();
    }
}