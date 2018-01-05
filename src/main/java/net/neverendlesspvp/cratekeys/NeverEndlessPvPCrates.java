package net.neverendlesspvp.cratekeys;

import com.google.gson.*;
import net.neverendlesspvp.cratekeys.command.SpigotCommandHandler;
import net.neverendlesspvp.cratekeys.commands.CrateCommand;
import net.neverendlesspvp.cratekeys.commands.CrateKeyCommand;
import net.neverendlesspvp.cratekeys.config.MeConfigManager;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.crate.Crate;
import net.neverendlesspvp.cratekeys.crate.CrateLocation;
import net.neverendlesspvp.cratekeys.inventory.MeInventoryClickListener;
import net.neverendlesspvp.cratekeys.inventory.MeInventoryCloseListener;
import net.neverendlesspvp.cratekeys.key.CrateKey;
import net.neverendlesspvp.cratekeys.key.reward.Rarity;
import net.neverendlesspvp.cratekeys.listeners.ClickListener;
import net.neverendlesspvp.cratekeys.listeners.PlayerInteractListener;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public final class NeverEndlessPvPCrates extends JavaPlugin {
    private static NeverEndlessPvPCrates instance;
    private static MeConfigManager configManager;
    private SpigotCommandHandler commandHandler;
    private Map<String, CrateKey> crateKeyMap;
    private Map<String, Rarity> rarityMap;
    private List<Integer> toCancelTaskIdList  =new ArrayList<>();
    private Map<UUID, ArmorStand> hologramMap;
    private Map<UUID, Crate> crateMap;
    private Map<UUID, Integer> taskIdPlayerMap;
    private Map<Location, UUID> crateLocationMap;
    private GsonBuilder gsonBuilder;

    public static NeverEndlessPvPCrates getInstance() {
        return instance;
    }

    public void cancelPlayerTask(UUID uuid) {
        if (taskIdPlayerMap.containsKey(uuid)) {
            toCancelTaskIdList.add(taskIdPlayerMap.get(uuid));
            taskIdPlayerMap.remove(uuid);
        }
    }

    public void setPlayerTaskId(UUID uuid, int taskId) {
        if (!taskIdPlayerMap.containsKey(uuid)) {
            taskIdPlayerMap.put(uuid, taskId);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        configManager = new MeConfigManager(this);
        configManager.load(new MessageConfig());
        this.taskIdPlayerMap = new HashMap<>();
        this.gsonBuilder = new GsonBuilder();
        this.hologramMap = new HashMap<>();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this,()->{
            for (Integer integer : toCancelTaskIdList) {
                Bukkit.getScheduler().cancelTask(integer);
            }
            toCancelTaskIdList.clear();
        },5L,5L);
        this.gsonBuilder.setDateFormat(MessageConfig.jsonBuilder_jsonDateFormat);
        this.gsonBuilder.setFieldNamingStrategy(f -> {
            switch (f.getName()) {
                case "rewardMap":
                    return "rewards";
                default:
                    return f.getName();
            }
        });
        this.registerListeners();
        this.loadRarities();
        this.loadCrateKeys();
        this.loadCrates();
        this.registerCommands();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new MeInventoryClickListener(), this);
        pluginManager.registerEvents(new MeInventoryCloseListener(), this);
        pluginManager.registerEvents(new ClickListener(), this);
    }

    private void saveCrates() {
        for (UUID uuid : crateMap.keySet()) {
            if (hologramMap.containsKey(uuid)) {
                hologramMap.get(uuid).remove();
                hologramMap.remove(uuid);
            }
        }
        File cratesFile = new File(getDataFolder(), "crates.json");
        if (cratesFile.exists()) {
            cratesFile.delete();
        }
        try {
            cratesFile.createNewFile();
            JsonArray jsonArray = new JsonArray();
            for (Crate crate : this.crateMap.values()) {
                jsonArray.add(new JsonParser().parse(this.gsonBuilder.create().toJson(crate)).getAsJsonObject());
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("crates", jsonArray);
            FileUtils.writeStringToFile(cratesFile, jsonObject.toString(), Charset.defaultCharset());
            System.out.println("Saved crates");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCrates() {
        this.crateMap = new HashMap<>();
        this.crateLocationMap = new HashMap<>();
        File cratesFile = new File(getDataFolder(), "crates.json");
        if (!cratesFile.exists()) {
            try {
                cratesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            String fileToString = FileUtils.readFileToString(cratesFile, Charset.defaultCharset());
            JsonObject parse = new JsonParser().parse(fileToString).getAsJsonObject();
            if (parse.has("crates")) {
                for (JsonElement jsonElement : parse.get("crates").getAsJsonArray()) {
                    JsonObject asJsonObject = jsonElement.getAsJsonObject();
                    Crate crate = this.gsonBuilder.create().fromJson(asJsonObject.toString(), Crate.class);
                    this.crateMap.put(crate.getUuid(), crate);
                    this.crateLocationMap.put(crate.getLocation().toLocation(), crate.getUuid());
                    System.out.println("Loaded crate " + crate.getUuid().toString());
                    createHologram(crate);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load crates.");
            e.printStackTrace();
        }
    }

    private void createHologram(Crate crate) {
        Location location = crate.getLocation().toLocation();
        location.getBlock().setType(Material.AIR);
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setMetadata("CrateUUID", new FixedMetadataValue(this, crate.getUuid().toString()));
        armorStand.setSmall(false);
        armorStand.setMarker(false);
        CrateKey crateKey = getCrateKey(crate.getCrateKey());
        String color = "&6";
        if (crateKey != null) {
            color = crateKey.getColor();
        }
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', MessageConfig.hologram_crateLine).replaceAll("%color%", ChatColor.translateAlternateColorCodes('&',color)).replaceAll("%crateName%", crate.getCrateKey().replaceAll("Key", " Crate")));
        armorStand.setCustomNameVisible(true);
        location.getBlock().setType(Material.CHEST);
        armorStand.teleport(new Location(location.getWorld(),location.getX(),location.getY(),location.getZ()).subtract(-0.3,0.6,-0.6));
        hologramMap.put(crate.getUuid(), armorStand);
    }

    private void registerCommands() {
        this.commandHandler = new SpigotCommandHandler();
        this.commandHandler.setDebug(true);
        this.commandHandler.registerCommand(new CrateKeyCommand());
        this.commandHandler.registerCommand(new CrateCommand());
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
            return;
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
//            System.out.println(crateKey.getName() + " Rewards");
//            for (Reward reward : crateKey.getRewardList()) {
//                System.out.println("  " + reward.getName() + ":");
//                System.out.println("    Chance:" + reward.getChance());
//                System.out.println("    Rarity:" + reward.getRarityObject().getName());
//                System.out.println("    Messages:");
//                System.out.println("      Broadcast: " + reward.getMessages().isBroadcast());
//                System.out.println("      BroadcastMessage: " + reward.getMessages().getBroadcastMessage());
//                System.out.println("      WinMessage: " + reward.getMessages().getWinMessage());
//                System.out.println("    Item:");
//                System.out.println("      Type: " + reward.getItem().getType().toString());
//                System.out.println("      DisplayName: " + reward.getItem().getDisplayName());
//                System.out.println("      Data: " + reward.getItem().getData());
//                System.out.println("      Lore:");
//                for (String s : reward.getItem().getLore()) {
//                    System.out.println("        " +  s);
//                }
//                System.out.println("    Commands:");
//                for (RewardCommand rewardCommand : reward.getCommands()) {
//                    System.out.println("      " +rewardCommand.getCommand() + ":" + rewardCommand.getChance() );
//                }
//
//            }
        });
    }

    @Override
    public void onDisable() {
        configManager.unload();
        this.saveCrates();
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

    public boolean isCrate(Block block) {
        return crateLocationMap.containsKey(block.getLocation());
    }

    public boolean deleteCrate(Block block) {
        UUID toDeleteUuid = null;
        for (UUID uuid : this.crateMap.keySet()) {
            Crate crate = crateMap.get(uuid);
            if (block.getLocation().equals(crate.getLocation().toLocation())) {
                toDeleteUuid = uuid;
                break;
            }
        }
        if (toDeleteUuid != null) {
            this.crateMap.remove(toDeleteUuid);
            if (hologramMap.containsKey(toDeleteUuid)) {
                hologramMap.get(toDeleteUuid).remove();
                hologramMap.remove(toDeleteUuid);
            }
            return true;
        }
        return false;
    }

    public boolean createCrate(Block block, CrateKey crateKey) {
        Crate crate = new Crate(UUID.randomUUID(), new CrateLocation(block.getLocation()), crateKey.getName());
        this.crateMap.put(crate.getUuid(), crate);
        this.crateLocationMap.put(crate.getLocation().toLocation(), crate.getUuid());
        createHologram(crate);
        return true;
    }

    public Crate getCrate(Block block) {
        if (crateLocationMap.containsKey(block.getLocation())) {
            UUID uuid = crateLocationMap.get(block.getLocation());
            return crateMap.get(uuid);
        }
        return null;
    }

    public void reload() {
        configManager.reload();
    }

    public List<CrateKey> getCrateKeyList() {
        return new ArrayList<>(crateKeyMap.values());
    }
}
