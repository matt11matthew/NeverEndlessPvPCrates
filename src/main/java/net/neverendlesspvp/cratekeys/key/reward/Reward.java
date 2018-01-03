package net.neverendlesspvp.cratekeys.key.reward;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.utilities.CrateItem;

import java.util.List;

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
}
