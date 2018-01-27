package net.neverendlesspvp.cratekeys.listeners;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("LOADING_META")) {
            player.removeMetadata("LOADING_META", NeverEndlessPvPCrates.getInstance());
        }
        player.setMetadata("LOADING_META", new FixedMetadataValue(NeverEndlessPvPCrates.getInstance(), true));
        new BukkitRunnable(){

            @Override
            public void run() {
                if (player.hasMetadata("LOADING_META")) {
                    player.removeMetadata("LOADING_META", NeverEndlessPvPCrates.getInstance());
                }
            }
        }.runTaskLater(NeverEndlessPvPCrates.getInstance(), 300L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        if (player.hasMetadata("OPENING_CRATE")) {
            player.removeMetadata("OPENING_CRATE", NeverEndlessPvPCrates.getInstance());
        }
        if (player.hasMetadata("LOADING_META")) {
            player.removeMetadata("LOADING_META", NeverEndlessPvPCrates.getInstance());
        }
    }
}