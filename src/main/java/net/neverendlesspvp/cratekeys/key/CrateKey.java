package net.neverendlesspvp.cratekeys.key;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.key.reward.Reward;
import net.neverendlesspvp.cratekeys.utilities.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class CrateKey {
    private String name;
    private CrateItem item;
    private Map<String, Reward> rewardMap;

    public CrateKey(String name, CrateItem item, Map<String, Reward> rewardMap) {
        this.name = name;
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
        boolean annimation = true;
        if (!annimation) {
            Map<Reward, Integer> rewardIntegerMap = new HashMap<>();
            for (Reward reward : rewardMap.values()) {
                rewardIntegerMap.put(reward, (int) reward.getChance());
            }
            List<Reward> rewards = RandomUtils.chooseRandomElements(rewardIntegerMap, MessageConfig.settings_cycleTimes);
            Reward win = rewards.get(rewards.size() - 1);
            win.giveReward(player);
        } else {
            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(NeverEndlessPvPCrates.getInstance(), new CrateOpenTask(player, this), MessageConfig.settings_cycleWait, MessageConfig.settings_cycleWait);
            NeverEndlessPvPCrates.getInstance().setPlayerTaskId(player.getUniqueId(), taskId);
        }
    }

    public Reward getRandomReward() {
        Map<Reward, Integer> rewardIntegerMap = new HashMap<>();
        for (Reward reward : rewardMap.values()) {
            rewardIntegerMap.put(reward, (int) reward.getChance());
        }
        List<Reward> rewards = RandomUtils.chooseRandomElements(rewardIntegerMap, 1);
        Reward reward = rewards.get(0);
        return reward;
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

        public CrateOpenTask(Player player, CrateKey crateKey) {
            this.player = player;
            this.uuid = player.getUniqueId();
            this.crateKey = crateKey;
            this.currentCycle = 0;
            this.inventory = Bukkit.createInventory(null, 9 * 3, ChatColor.translateAlternateColorCodes('&', MessageConfig.crateOpeningInventory_title).replaceAll("%crateName%", crateKey.getName()));
            currentReward = crateKey.getRandomReward();
            lastReward = currentReward;
            inventory.setItem(MessageConfig.crateOpeningInventory_currentRewardSlot, currentReward.getItem().toItemStack());
            this.maxCycle = MessageConfig.settings_cycleTimes;
        }

        @Override
        public void run() {
            if (player == null || !player.isOnline()) {
                NeverEndlessPvPCrates.getInstance().cancelPlayerTask(uuid);
                return;
            }
            if (!opened) {
                player.openInventory(inventory);
                opened = true;
            }
            if (player.getOpenInventory()!=null&&(player.getOpenInventory().getTopInventory()!=null)&&(!player.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase(inventory.getTitle()))) {
                player.openInventory(inventory);
                opened = true;
            }
            while (lastReward == currentReward) {
                currentReward = crateKey.getRandomReward();
            }
            inventory.setItem(MessageConfig.crateOpeningInventory_currentRewardSlot, currentReward.getItem().toItemStack());
            player.playSound(player.getLocation(), Sound.valueOf(MessageConfig.sounds_crateTickSound_sound.toUpperCase()),(float) MessageConfig.sounds_crateTickSound_volume, (float) MessageConfig.sounds_crateTickSound_pitch);
            lastReward = currentReward;
            currentCycle++;
            if (currentCycle >= maxCycle) {
                if (!rewarded) {
                    currentReward.giveReward(player);
                    rewarded = true;
                    player.closeInventory();
                }
                NeverEndlessPvPCrates.getInstance().cancelPlayerTask(player.getUniqueId());
            }
        }
    }
}