package net.neverendlesspvp.cratekeys.key.reward;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class Rarity {
    private String name;
    private String chatColor;
    private int value;

    public Rarity(String name, String chatColor, int value) {
        this.name = name;
        this.chatColor = chatColor;
        this.value = value;
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
     * Getter for property 'chatColor'.
     *
     * @return Value for property 'chatColor'.
     */
    public String getChatColor() {
        return chatColor;
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public int getValue() {
        return value;
    }
}
