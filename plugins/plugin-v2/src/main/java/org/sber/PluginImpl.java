package org.sber;

import org.sber.plugin.Plugin;

public class PluginImpl implements Plugin {
    @Override
    public void doUseful() {
        System.out.println("Plugin v2");
    }
}
