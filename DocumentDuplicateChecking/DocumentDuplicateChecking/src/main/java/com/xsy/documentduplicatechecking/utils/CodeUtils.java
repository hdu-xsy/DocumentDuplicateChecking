package com.xsy.documentduplicatechecking.utils;

import java.util.Random;

/**
 * @author xushiyue
 * @date 2019年8月1日11:03:54
 */
public class CodeUtils {

    private static final Random RANDOM = new Random();

    private static final int BIT = 4;

    public static String getVerificationCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BIT; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return new String(sb);
    }
}
