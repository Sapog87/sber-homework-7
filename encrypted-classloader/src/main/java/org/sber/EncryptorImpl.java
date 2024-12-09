package org.sber;

import java.util.Arrays;

public class EncryptorImpl implements Encryptor {
    @Override
    public byte[] encrypt(byte[] data, int key) {
        byte[] result = Arrays.copyOf(data, data.length);
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (result[i] - key);
        }
        return result;
    }
}
