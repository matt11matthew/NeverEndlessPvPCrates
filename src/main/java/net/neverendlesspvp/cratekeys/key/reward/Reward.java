package net.neverendlesspvp.cratekeys.key.reward;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.key.CrateItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class Reward {
    private String name;
    private double chance;
    private List<RewardCommand> commands;
    private CrateItem item;
    private String rarity;
    private RewardMessage messages;

    public Reward(String name, double chance, List<RewardCommand> commands, CrateItem item, String rarity, RewardMessage messages) {
        this.name = name;
        this.chance = chance;
        this.commands = commands;
        this.item = item;
        this.rarity = rarity;
        this.messages = messages;
        updateName();
    }

    public void updateName() {
        Rarity rarityObject = getRarityObject();
        if (rarityObject != null) {
            this.item.setDisplayName(ChatColor.translateAlternateColorCodes('&',this.item.getDisplayName().replaceAll(ChatColor.COLOR_CHAR+"", "&a").replaceAll("%rarity%", rarityObject.getChatColor() + rarityObject.getName())));
        }
    }

    public Rarity getRarityObject() {
        return NeverEndlessPvPCrates.getInstance().getRarity(rarity);
    }

    /**
     * Getter for property 'messages'.
     *
     * @return Value for property 'messages'.
     */
    public RewardMessage getMessages() {
        return messages;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for property 'chance'.
     *
     * @return Value for property 'chance'.
     */
    public double getChance() {
        return chance;
    }

    /**
     * Getter for property 'commands'.
     *
     * @return Value for property 'commands'.
     */
    public List<RewardCommand> getCommands() {
        return commands;
    }

    public CrateItem getItem() {
        return item;
    }

    public String getRarity() {
        return rarity;
    }

    public void giveReward(Player player) {
        if (messages.isBroadcast()) {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', messages.getBroadcastMessage()).replaceAll("%player%", player.getName()));
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getWinMessage()));
        List<String> commandsToRunList = new ArrayList<>();
        for (RewardCommand rewardCommand :getCommands()) {
            if (rewardCommand.getChance() >= 100) {
                commandsToRunList.add(rewardCommand.getCommand());
            } else if (new Random().nextInt(100) <= rewardCommand.getChance()) {
                commandsToRunList.add(rewardCommand.getCommand());
            }
        }
        for (String command : commandsToRunList) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
        }
    }
}
