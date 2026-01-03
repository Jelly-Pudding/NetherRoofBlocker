package com.jellypudding.netherRoofBlocker;

import org.bukkit.plugin.java.JavaPlugin;

public final class NetherRoofBlocker extends JavaPlugin {

    @Override
    public void onEnable() {
        // Sets up bstats
        int pluginId = 28683;
        Metrics metrics = new Metrics(this, pluginId);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
