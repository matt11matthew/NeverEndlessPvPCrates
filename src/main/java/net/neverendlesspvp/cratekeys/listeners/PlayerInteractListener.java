package net.neverendlesspvp.cratekeys.listeners;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.crate.Crate;
import net.neverendlesspvp.cratekeys.inventory.MeInventory;
import net.neverendlesspvp.cratekeys.inventory.item.ClickHandler;
import net.neverendlesspvp.cratekeys.inventory.item.MeInventoryItem;
import net.neverendlesspvp.cratekeys.key.CrateKey;
import net.neverendlesspvp.cratekeys.key.reward.Reward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class PlayerInteractListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (NeverEndlessPvPCrates.getInstance().isCrate(block)) {
                event.setCancelled(true);
                Crate crate = NeverEndlessPvPCrates.getInstance().getCrate(block);
                if (crate != null) {
                    CrateKey crateKey = NeverEndlessPvPCrates.getInstance().getCrateKey(crate.getCrateKey());
                    if (crateKey != null) {
                        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            MeInventory meInventory = new MeInventory(ChatColor.translateAlternateColorCodes('&', MessageConfig.crateRewardInventory_title).replaceAll("%crateName%", crate.getCrateKey()), 9 * 3);
                            int slot = 0;
                            for (Reward reward : crateKey.getRewardList()) {
                                meInventory.setItem(slot, new MeInventoryItem(reward.getItem().toItemStack())
                                        .addLore(MessageConfig.crateRewardInventory_lore.replaceAll("%percent%", (int) reward.getChance() + ""))
                                        .withClickHandler(new ClickHandler((player1, itemStack, slot1) -> {

                                        }, ClickType.values()).lock()));
                                slot++;

                            }
                            meInventory.open(player);
                        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            ItemStack itemInHand = player.getItemInHand();
                            if (itemInHand != null && (itemInHand.getType() != Material.AIR) && crateKey.getItem().toItemStack().isSimilar(itemInHand)) {
                                if (itemInHand.getAmount() > 1) {
                                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                                } else {
                                    player.setItemInHand(new ItemStack(Material.AIR));
                                }
                                crateKey.openCrate(player);
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_notCorrectKey).replaceAll("%key%", crateKey.getName()));
                            }
                        }
                    }
                }
            }
        }
    }
}
