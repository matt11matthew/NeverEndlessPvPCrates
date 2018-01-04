package net.neverendlesspvp.cratekeys.crate;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class CrateLocation {
    private String world;
    private int x;
    private int y;
    private int z;

    public CrateLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CrateLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(this.world),x,y,z);
    }

    /**
     * Getter for property 'world'.
     *
     * @return Value for property 'world'.
     */
    public String getWorld() {
        return world;
    }

    /**
     * Getter for property 'x'.
     *
     * @return Value for property 'x'.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for property 'y'.
     *
     * @return Value for property 'y'.
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for property 'z'.
     *
     * @return Value for property 'z'.
     */
    public int getZ() {
        return z;
    }
}
