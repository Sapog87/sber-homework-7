package org.sber;

public interface Encryptor {
    byte[] encrypt(byte[] data, int key);
}
