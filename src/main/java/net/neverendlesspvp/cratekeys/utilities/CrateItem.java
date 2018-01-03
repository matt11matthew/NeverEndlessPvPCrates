package net.neverendlesspvp.cratekeys.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class CrateItem {
    private Material type;
    private int data;
    private String displayName;
    private String[] lore;

    public CrateItem(Material type, int data, String displayName, String[] lore) {
        this.type = type;
        this.data = data;
        this.displayName = displayName;
        this.lore = lore;
    }

    public ItemStack toItemStack(int amount) {
        ItemStack itemStack = new ItemStack(this.type, amount, (short) this.data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.stream(this.lore).map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.displayName));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    /**
     * Getter for property 'type'.
     *
     * @return Value for property 'type'.
     */
    public Material getType() {
        return type;
    }

    /**
     * Getter for property 'data'.
     *
     * @return Value for property 'data'.
     */
    public int getData() {
        return data;
    }

    /**
     * Getter for property 'displayName'.
     *
     * @return Value for property 'displayName'.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter for property 'lore'.
     *
     * @return Value for property 'lore'.
     */
    public String[] getLore() {
        return lore;
    }
}
