package org.sber.plugin;

public interface PluginManager {
    Plugin load(String pluginName, String pluginClassName);
}
