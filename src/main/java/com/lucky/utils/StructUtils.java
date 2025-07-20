package com.lucky.utils;

public class StructUtils {
    public static long BytestoLong(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0L;
        }
        byte[] targetBytes = new byte[8];
        System.arraycopy(bytes, 0, targetBytes, 0, Math.min(bytes.length, 8));

        long result = 0;
        for (int i = 0; i < 8; i++) {
            // 每个字节转换为无符号 int（0-255），然后左移 (56 - i*8) 位
            result |= ((targetBytes[i] & 0xFFL) << (56 - i * 8));
        }
        return result;

    }
}
