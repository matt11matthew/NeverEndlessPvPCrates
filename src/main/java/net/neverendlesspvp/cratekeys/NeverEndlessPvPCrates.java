package net.neverendlesspvp.cratekeys;

import org.bukkit.plugin.java.JavaPlugin;

public final class NeverEndlessPvPCrates extends JavaPlugin {
    private static NeverEndlessPvPCrates instance;

    public static NeverEndlessPvPCrates getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
