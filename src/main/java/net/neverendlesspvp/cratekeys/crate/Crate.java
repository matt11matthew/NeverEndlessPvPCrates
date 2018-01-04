package net.neverendlesspvp.cratekeys.crate;

import java.util.UUID;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class Crate {
    private UUID uuid;
    private CrateLocation location;
    private String crateKey;

    public Crate(UUID uuid, CrateLocation location, String crateKey) {
        this.uuid = uuid;
        this.location = location;
        this.crateKey = crateKey;
    }

    /**
     * Getter for property 'uuid'.
     *
     * @return Value for property 'uuid'.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Getter for property 'location'.
     *
     * @return Value for property 'location'.
     */
    public CrateLocation getLocation() {
        return location;
    }

    /**
     * Getter for property 'crateKey'.
     *
     * @return Value for property 'crateKey'.
     */
    public String getCrateKey() {
        return crateKey;
    }
}
