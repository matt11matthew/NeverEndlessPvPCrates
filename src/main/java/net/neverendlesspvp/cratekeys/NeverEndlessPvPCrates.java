package net.neverendlesspvp.cratekeys;

import com.google.gson.GsonBuilder;
import net.neverendlesspvp.cratekeys.config.MeConfigManager;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.key.CrateKey;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class NeverEndlessPvPCrates extends JavaPlugin {
    private static NeverEndlessPvPCrates instance;
    private static MeConfigManager configManager;
    private Map<String, CrateKey> crateKeyMap;
    private GsonBuilder gsonBuilder;

    public static NeverEndlessPvPCrates getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        configManager = new MeConfigManager(this);
        configManager.load(new MessageConfig());
        this.gsonBuilder = new GsonBuilder();
        this.gsonBuilder.setDateFormat(MessageConfig.jsonBuilder_jsonDateFormat);
        this.gsonBuilder.setFieldNamingStrategy(f -> {
            switch (f.getName()) {
                case "rewardMap":
                    return "rewards";
                default:
                    return f.getName();
            }
        });
        this.loadCrateKeys();
    }

    private void loadCrateKeys() {
        this.crateKeyMap = new HashMap<>();
        File keyDirectory = new File(getDataFolder() + "/keys/");
        if (!keyDirectory.exists()) {
            keyDirectory.mkdirs();
        }
        for (File file : Objects.requireNonNull(keyDirectory.listFiles())) {
            if (file.getName().endsWith(".json")) {
                try {
                    CrateKey crateKey = gsonBuilder.create().fromJson(FileUtils.readFileToString(file, Charset.defaultCharset()), CrateKey.class);
                    this.crateKeyMap.put(crateKey.getName(), crateKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.crateKeyMap.values().forEach(crateKey -> {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[NeverEndlessPvPCrates] Loaded " + ChatColor.GRAY + crateKey.getName() + ChatColor.GREEN + ".");
        });
    }

    @Override
    public void onDisable() {
        configManager.unload();
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }
}
