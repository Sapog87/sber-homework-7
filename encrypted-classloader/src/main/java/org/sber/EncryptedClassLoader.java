package org.sber;

import java.io.File;
import java.util.Arrays;

public class EncryptedClassLoader extends AbstractEncryptedClassLoader<Integer> {
    public EncryptedClassLoader(Integer key, File dir, ClassLoader parent) {
        super(key, dir, parent);
    }

    @Override
    protected byte[] decrypt(byte[] data) {
        byte[] result = Arrays.copyOf(data, data.length);
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (result[i] + key);
        }
        return result;
    }
}
