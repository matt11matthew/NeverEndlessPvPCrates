package net.neverendlesspvp.cratekeys;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.neverendlesspvp.cratekeys.command.SpigotCommandHandler;
import net.neverendlesspvp.cratekeys.commands.CrateKeyCommand;
import net.neverendlesspvp.cratekeys.config.MeConfigManager;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.key.CrateKey;
import net.neverendlesspvp.cratekeys.key.reward.Reward;
import net.neverendlesspvp.cratekeys.key.reward.RewardCommand;
import net.neverendlesspvp.cratekeys.key.reward.Rarity;
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
    private SpigotCommandHandler commandHandler;
    private Map<String, CrateKey> crateKeyMap;
    private Map<String, Rarity> rarityMap;
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
        this.loadRarities();
        this.loadCrateKeys();
        this.registerCommands();
    }

    private void registerCommands() {
        this.commandHandler = new SpigotCommandHandler();
        this.commandHandler.setDebug(true);
        this.commandHandler.registerCommand(new CrateKeyCommand());
        this.commandHandler.registerCommands();
    }

    private void loadRarities() {
        this.rarityMap = new HashMap<>();
        File rarityFile = new File(getDataFolder(), "rarities.json");
        if (!rarityFile.exists()) {
            try {
                rarityFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String fileToString = FileUtils.readFileToString(rarityFile, Charset.defaultCharset());
            JsonObject parse = new JsonParser().parse(fileToString).getAsJsonObject();
            if (parse.has("rarities")) {
                for (JsonElement jsonElement : parse.get("rarities").getAsJsonArray()) {
                    JsonObject asJsonObject = jsonElement.getAsJsonObject();
                    Rarity rarity = gsonBuilder.create().fromJson(asJsonObject.toString(), Rarity.class);
                    rarityMap.put(rarity.getName(), rarity);
                    System.out.println("Loaded rarity " + rarity.getName());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load rarities.");
            e.printStackTrace();
        }

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
            System.out.println(crateKey.getName() + " Rewards");
            for (Reward reward : crateKey.getRewardList()) {
                System.out.println("  " + reward.getName() + ":");
                System.out.println("    Chance:" + reward.getChance());
                System.out.println("    Rarity:" + reward.getRarityObject().getName());
                System.out.println("    Messages:");
                System.out.println("      Broadcast: " + reward.getMessages().isBroadcast());
                System.out.println("      BroadcastMessage: " + reward.getMessages().getBroadcastMessage());
                System.out.println("      WinMessage: " + reward.getMessages().getWinMessage());
                System.out.println("    Item:");
                System.out.println("      Type: " + reward.getItem().getType().toString());
                System.out.println("      DisplayName: " + reward.getItem().getDisplayName());
                System.out.println("      Data: " + reward.getItem().getData());
                System.out.println("      Lore:");
                for (String s : reward.getItem().getLore()) {
                    System.out.println("        " +  s);
                }
                System.out.println("    Commands:");
                for (RewardCommand rewardCommand : reward.getCommands()) {
                    System.out.println("      " +rewardCommand.getCommand() + ":" + rewardCommand.getChance() );
                }

            }
        });
    }

    @Override
    public void onDisable() {
        configManager.unload();
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public Rarity getRarity(String rarity) {
        return rarityMap.get(rarity);
    }

    public CrateKey getCrateKey(String key) {
        for (CrateKey crateKey : crateKeyMap.values()) {
            if (crateKey.getName().equalsIgnoreCase(key)) {
                return crateKey;
            }
        }
        return null;
    }
}
