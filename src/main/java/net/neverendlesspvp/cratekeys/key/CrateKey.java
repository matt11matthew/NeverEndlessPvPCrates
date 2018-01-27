package net.neverendlesspvp.cratekeys.key;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.key.reward.Reward;
import net.neverendlesspvp.cratekeys.utilities.RandomUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class CrateKey {
    private String name;
    private CrateItem item;
    private Map<String, Reward> rewardMap;
    private String color;

    public CrateKey(String name, CrateItem item, Map<String, Reward> rewardMap, String color) {
        this.name = name;
        this.color = color;
        this.item = item;
        this.rewardMap = rewardMap;
    }

    /**
     * Getter for property 'item'.
     *
     * @return Value for property 'item'.
     */
    public CrateItem getItem() {
        return item;
    }

    /**
     * Getter for property 'rewardMap'.
     *
     * @return Value for property 'rewardMap'.
     */
    public Map<String, Reward> getRewardMap() {
        return rewardMap;
    }


    /**
     * Getter for property 'rewardList'.
     *
     * @return Value for property 'rewardList'.
     */
    public List<Reward> getRewardList() {
        return new ArrayList<>(rewardMap.values());
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    public void openCrate(Player player) {
        if (player.hasMetadata("OPENING_CRATE")) {
            return;
        }

        player.setMetadata("OPENING_CRATE", new FixedMetadataValue(NeverEndlessPvPCrates.getInstance(), true));
        if (!MessageConfig.settings_animation) {
            Map<Reward, Integer> rewardIntegerMap = new HashMap<>();
            for (Reward reward : rewardMap.values()) {
                rewardIntegerMap.put(reward, (int) reward.getChance());
            }
            List<Reward> rewards = RandomUtils.chooseRandomElements(rewardIntegerMap, MessageConfig.settings_cycleTimes);
            Reward win = rewards.get(rewards.size() - 1);
            win.giveReward(player);
            player.removeMetadata("OPENING_CRATE", NeverEndlessPvPCrates.getInstance());
        } else {
            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(NeverEndlessPvPCrates.getInstance(), new CrateOpenTask(player, this), MessageConfig.settings_cycleWait/2, MessageConfig.settings_cycleWait/2);
            NeverEndlessPvPCrates.getInstance().setPlayerTaskId(player.getUniqueId(), taskId);
        }
    }

    public Reward getRandomReward() {
        Map<Reward, Integer> rewardIntegerMap = new HashMap<>();
        for (Reward reward : rewardMap.values()) {
            rewardIntegerMap.put(reward, (int) reward.getChance());
        }
        List<Reward> rewards = RandomUtils.chooseRandomElements(rewardIntegerMap, 1);
        return rewards.get(0);
    }

    public String getColor() {
        return color;
    }

    public class CrateOpenTask extends BukkitRunnable {
        private Player player;
        private UUID uuid;
        private CrateKey crateKey;
        private int currentCycle;
        private int maxCycle;
        private Inventory inventory;
        private Reward currentReward;
        private Reward lastReward;
        private boolean rewarded = false;
        private boolean opened = false;
        private long lastTime;

        public CrateOpenTask(Player player, CrateKey crateKey) {
            this.player = player;
            this.uuid = player.getUniqueId();
            this.crateKey = crateKey;
            this.currentCycle = 0;
            lastTime = System.currentTimeMillis();
            this.inventory = Bukkit.createInventory(null, 9 * 3, ChatColor.translateAlternateColorCodes('&', MessageConfig.crateOpeningInventory_title).replaceAll("%crateName%", crateKey.getName()));
            currentReward = crateKey.getRandomReward();
            lastReward = currentReward;
          //  1 tick = 50 milliseconds
            inventory.setItem(MessageConfig.crateOpeningInventory_currentRewardSlot, currentReward.getItem().toItemStack());
            for (int i = 0; i < inventory.getSize(); i++) {
                if (i == MessageConfig.crateOpeningInventory_currentRewardSlot) {
                    continue;
                }
                inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getWoolData()));
            }
            this.maxCycle = MessageConfig.settings_cycleTimes;
        }

        @Override
        public void run() {
            if (player == null || !player.isOnline()) {
                NeverEndlessPvPCrates.getInstance().cancelPlayerTask(uuid);
                return;
            }
            if (!opened && !rewarded) {
                player.openInventory(inventory);
                opened = true;
            }
            if (player.getOpenInventory()!=null&& !rewarded&&(player.getOpenInventory().getTopInventory()!=null)&&(!player.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase(inventory.getTitle()))) {
                player.openInventory(inventory);
                opened = true;
            }
            while (lastReward == currentReward) {
                currentReward = crateKey.getRandomReward();
            }
            Map<DyeColor, Integer> dyeColorIntegerMap = new HashMap<>();
            List<DyeColor> dyeColors = Arrays.asList(DyeColor.values());//Arrays.asList(DyeColor.PINK, DyeColor.LIGHT_BLUE, DyeColor.YELLOW, DyeColor.PURPLE, DyeColor.GREEN);

            for (DyeColor dyeColor : dyeColors) {
                dyeColorIntegerMap.put(dyeColor, (100 / dyeColors.size()));
            }
            for (int i = 0; i < inventory.getSize(); i++) {
                if (i == MessageConfig.crateOpeningInventory_currentRewardSlot) {
                    continue;
                }
                inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, RandomUtils.chooseRandomElements(dyeColorIntegerMap, 1).get(0).getWoolData()));
            }
            if ((System.currentTimeMillis()-lastTime)>=50*MessageConfig.settings_cycleWait) {
                player.playSound(player.getLocation(), Sound.valueOf(MessageConfig.sounds_crateTickSound_sound.toUpperCase()),(float) MessageConfig.sounds_crateTickSound_volume, (float) MessageConfig.sounds_crateTickSound_pitch);
                inventory.setItem(MessageConfig.crateOpeningInventory_currentRewardSlot, currentReward.getItem().toItemStack());
                lastTime= System.currentTimeMillis();
                lastReward = currentReward;
                currentCycle++;
                if (currentCycle >= maxCycle) {
                    if (!rewarded) {
                        currentReward.giveReward(player);
                        rewarded = true;
                        player.closeInventory();
                        if (player.hasMetadata("OPENING_CRATE")) {
                            player.removeMetadata("OPENING_CRATE", NeverEndlessPvPCrates.getInstance());
                        }
                    }
                    NeverEndlessPvPCrates.getInstance().cancelPlayerTask(player.getUniqueId());
                }
            }
        }
    }
}