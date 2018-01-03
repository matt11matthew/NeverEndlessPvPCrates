package net.neverendlesspvp.cratekeys.key.reward;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class RewardCommand {
    private double chance;
    private String command;

    public RewardCommand(double chance, String command) {
        this.chance = chance;
        this.command = command;
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
     * Getter for property 'command'.
     *
     * @return Value for property 'command'.
     */
    public String getCommand() {
        return command;
    }
}
