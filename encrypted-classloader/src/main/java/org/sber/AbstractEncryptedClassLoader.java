package org.sber;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class AbstractEncryptedClassLoader<T> extends ClassLoader {
    protected final T key;
    private final File dir;

    public AbstractEncryptedClassLoader(T key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            File file = new File(dir, name);
            byte[] data = Files.readAllBytes(file.toPath());
            byte[] decrypted = decrypt(data);
            return defineClass(name, decrypted, 0, decrypted.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("класс %s не найден".formatted(name), e);
        }
    }

    protected abstract byte[] decrypt(byte[] data);
}
