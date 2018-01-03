package net.neverendlesspvp.cratekeys.key.reward;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class RewardMessage {
    private boolean broadcast;
    private String winMessage;
    private String broadcastMessage;

    public RewardMessage(boolean broadcast, String winMessage, String broadcastMessage) {
        this.broadcast = broadcast;
        this.winMessage = winMessage;
        this.broadcastMessage = broadcastMessage;
    }

    /**
     * Getter for property 'broadcast'.
     *
     * @return Value for property 'broadcast'.
     */
    public boolean isBroadcast() {
        return broadcast;
    }

    /**
     * Getter for property 'winMessage'.
     *
     * @return Value for property 'winMessage'.
     */
    public String getWinMessage() {
        return winMessage;
    }

    /**
     * Getter for property 'broadcastMessage'.
     *
     * @return Value for property 'broadcastMessage'.
     */
    public String getBroadcastMessage() {
        return broadcastMessage;
    }
}
