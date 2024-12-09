package org.sber;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EncryptedClassLoaderTest {

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        Encryptor encryptor = new EncryptorImpl();
        String fileToEncrypt = "./src/test/resources/test/ForEncryptionClass.class";
        Path path = Path.of(fileToEncrypt);
        byte[] data = Files.readAllBytes(path);
        byte[] result = encryptor.encrypt(data, 10);
        Files.write(Path.of("./src/test/resources/test/ForEncryptionClass"), result);
    }


    @Test
    @DisplayName("При верном ключе класс успешно загружается")
    void givenEncryptedClassloader_whenLoadingClassWithRightKey_thenSuccess() {
        ClassLoader encryptedClassLoader = new EncryptedClassLoader(
                10,
                new File("./src/test/resources/test"),
                ClassLoader.getSystemClassLoader()
        );

        Class<?> clazz = assertDoesNotThrow(() -> encryptedClassLoader.loadClass("ForEncryptionClass"));

        assertEquals("ForEncryptionClass", clazz.getName());
    }

    @Test
    @DisplayName("При неверном ключе кидается исключение")
    void givenEncryptedClassloader_whenLoadingClassWithWrongKey_thenException() {
        ClassLoader encryptedClassLoader = new EncryptedClassLoader(
                11,
                new File("./src/test/resources/test"),
                ClassLoader.getSystemClassLoader()
        );

        assertThrows(ClassFormatError.class, () -> encryptedClassLoader.loadClass("ForEncryptionClass"));
    }

}