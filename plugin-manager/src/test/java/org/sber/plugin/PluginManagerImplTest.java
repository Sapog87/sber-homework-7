package org.sber.plugin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sber.plugin.exception.PluginException;

import static org.junit.jupiter.api.Assertions.*;

class PluginManagerImplTest {

    @Test
    @DisplayName("Классы с одинаковыми именами загружены успешно")
    void givenPluginManager_whenLoadPluginsWithTheSameNames_thenSuccess() {
        String rootFolder = "./src/test/resources/plugins";
        PluginManager pluginManager = new PluginManagerImpl(rootFolder);

        Plugin plugin1 = pluginManager.load("plugin1", "org.sber.PluginImpl");
        Plugin plugin2 = pluginManager.load("plugin2", "org.sber.PluginImpl");

        assertNotNull(plugin1);
        assertNotNull(plugin2);
    }


    @Test
    @DisplayName("Исключение, если класс не имплементируют Plugin")
    void givenPluginManager_whenLoadWrongPlugin_thenException() {
        String rootFolder = "./src/test/resources/plugins";
        PluginManager pluginManager = new PluginManagerImpl(rootFolder);

        assertThrows(PluginException.class, () -> pluginManager.load("plugin3", "org.sber.PluginImpl"));
    }

    @Test
    @DisplayName("Null, если не нашли плагин в заданной папке")
    void givenPluginManager_whenLoadNonexistentPlugin_thenNull() {
        String rootFolder = "./src/test/resources/plugins";
        PluginManager pluginManager = new PluginManagerImpl(rootFolder);

        Plugin plugin1 = pluginManager.load("plugin1", "org.sber.NonexistentPluginImpl");

        assertNull(plugin1);
    }

    @Test
    @DisplayName("Исключение, если папки не существует")
    void givenPluginManager_whenLoadFromNonexistentFolder_thenException() {
        String rootFolder = "./src/test/resources/plugins";
        PluginManager pluginManager = new PluginManagerImpl(rootFolder);

        assertThrows(IllegalArgumentException.class, () -> pluginManager.load("nonexistentPlugin", "org.sber.PluginImpl"));
    }
}