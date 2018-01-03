package net.neverendlesspvp.cratekeys.key;

import java.util.List;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class Reward {
    private String name;
    private double chance;
    private List<RewardCommand> commands;

    public Reward(String name, double chance, List<RewardCommand> commands) {
        this.name = name;
        this.chance = chance;
        this.commands = commands;
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
}
