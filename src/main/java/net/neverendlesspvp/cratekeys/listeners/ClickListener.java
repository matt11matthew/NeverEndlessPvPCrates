package net.neverendlesspvp.cratekeys.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class ClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.OUTSIDE && event.getInventory().getType() != InventoryType.PLAYER && (event.getInventory().getTitle().startsWith("Opening"))) {
            event.setCancelled(true);
        }
    }
}