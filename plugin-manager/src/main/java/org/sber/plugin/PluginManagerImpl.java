package org.sber.plugin;

import lombok.RequiredArgsConstructor;
import org.sber.plugin.exception.PluginException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

@RequiredArgsConstructor
public class PluginManagerImpl implements PluginManager {
    private final String pluginRootDirectory;

    @Override
    public Plugin load(String pluginName, String pluginClassName) {
        File pluginFolder = new File(pluginRootDirectory, pluginName);
        if (!pluginFolder.exists()) {
            throw new IllegalArgumentException("директории %s не существует".formatted(pluginFolder));
        }
        if (!pluginFolder.isDirectory()) {
            throw new IllegalArgumentException("%s не является директорией".formatted(pluginFolder));
        }

        File[] files = pluginFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                try (URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()})) {
                    Class<?> clazz = classLoader.loadClass(pluginClassName);
                    Object object = clazz.getDeclaredConstructor().newInstance();
                    if (object instanceof Plugin plugin) {
                        return plugin;
                    }
                    throw new PluginException("переданный класс %s не является Plugin".formatted(pluginClassName));
                } catch (ClassNotFoundException ignored) {
                    // не нашли в текущем jar
                } catch (ReflectiveOperationException e) {
                    throw new PluginException("ошибка инстанцирования класса %s", e);
                } catch (IOException e) {
                    throw new PluginException(e);
                }
            }
        }

        return null;
    }
}
