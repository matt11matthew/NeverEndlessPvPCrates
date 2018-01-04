package net.neverendlesspvp.cratekeys.commands;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.command.Command;
import net.neverendlesspvp.cratekeys.command.SpigotCommand;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.key.CrateKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Matthew E on 1/3/2018.
 */
@Command(name = "crate", description = "The cratecommand")
public class CrateCommand extends SpigotCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(MessageConfig.permissions_crateCommand_base)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateCommand_base));
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
                switch (args[0].toLowerCase()) {
                    case "reload":
                        if (player.isOp()) {
                            NeverEndlessPvPCrates.getInstance().reload();
                        }
                            break;
                    case "delete":
                        onDeleteCrateCommand(player);
                        break;
                    case "create":
                        if (args.length >= 2) {
                            onCreateCrateCommand(player, args[1]);
                        }
                        break;
                }
            }
        }
    }

    private void onCreateCrateCommand(Player player, String key) {
        if (!player.hasPermission(MessageConfig.permissions_crateCommand_create)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateCommand_create));
            return;
        }
        NeverEndlessPvPCrates instance = NeverEndlessPvPCrates.getInstance();
        CrateKey crateKey = instance.getCrateKey(key);
        if (crateKey == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_create_invalidKey).replaceAll("%key%", key));
            return;
        }
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 100);
        if (targetBlock != null && (targetBlock.getType() == Material.CHEST)) {
            if (instance.isCrate(targetBlock)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_noCrateAlreadyExists));
                return;
            }
            instance.createCrate(targetBlock, crateKey);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_create));

        }
    }

    private void onDeleteCrateCommand(Player player) {
        if (!player.hasPermission(MessageConfig.permissions_crateCommand_delete)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateCommand_delete));
            return;
        }
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 100);
        if (targetBlock != null && (targetBlock.getType() == Material.CHEST)) {
            NeverEndlessPvPCrates instance = NeverEndlessPvPCrates.getInstance();
            if (!instance.isCrate(targetBlock)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_noCrateExists));
                return;
            }
            boolean b = instance.deleteCrate(targetBlock);
            if (b) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateCommand_deleteCrate));
            }
        }
    }
}
