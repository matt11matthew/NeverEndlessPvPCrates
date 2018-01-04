package net.neverendlesspvp.cratekeys.commands;

import net.neverendlesspvp.cratekeys.NeverEndlessPvPCrates;
import net.neverendlesspvp.cratekeys.command.Command;
import net.neverendlesspvp.cratekeys.command.SpigotCommand;
import net.neverendlesspvp.cratekeys.configs.MessageConfig;
import net.neverendlesspvp.cratekeys.key.CrateKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 1/3/2018.
 */
@Command(name = "cratekey", aliases = {"keys", "key"}, description = "The crate key command")
public class CrateKeyCommand extends SpigotCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(MessageConfig.permissions_crateKeyCommand_base)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateKeyCommand_base));
            return;
        }
        if (args.length == 0) {
            sendHelpMessage(sender);
        }
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "giveall":
                    onCrateKeyGiveAll(sender, args);
                    break;
                case "help":
                    sendHelpMessage(sender);
                    break;
                case "list":
                    if (!sender.hasPermission(MessageConfig.permissions_crateKeyCommand_list)) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateKeyCommand_list));
                        return;
                    }
                    for (CrateKey crateKey : NeverEndlessPvPCrates.getInstance().getCrateKeyList()) {
                        sender.sendMessage(crateKey.getName());
                    }
                    break;
                default:
                    sendHelpMessage(sender);
                    break;
            }
        }
    }

    private void onCrateKeyGiveAll(CommandSender sender, String[] args) {
        if (!sender.hasPermission(MessageConfig.permissions_crateKeyCommand_giveAll)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateKeyCommand_giveAll));
            return;
        }
        if (args.length >= 2) {
            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_giveAll_invalidAmount).replaceAll("%amount%", args[2]));
                    return;
                }
                if (amount > MessageConfig.settings_crateKeyCommand_giveAll_maxAmount) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_giveAll_invalidAmount).replaceAll("%amount%", args[2]));
                    return;
                }
            }
            String key = args[1];
            NeverEndlessPvPCrates instance = NeverEndlessPvPCrates.getInstance();
            CrateKey crateKey = instance.getCrateKey(key);
            int count = 0;
            if (crateKey == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_giveAll_invalidKey).replaceAll("%key%", key));
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getInventory().addItem(crateKey.getItem().toItemStack(amount));
                count++;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_giveAll_giveAll).replaceAll("%amount%", amount+"").replaceAll("%key%", crateKey.getName()).replaceAll("%playerCount%", count+  ""));
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        if (!sender.hasPermission(MessageConfig.permissions_crateKeyCommand_help)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageConfig.messages_crateKeyCommand_noPermission).replaceAll("%permission%", MessageConfig.permissions_crateKeyCommand_help));
            return;
        }
        for (String message : MessageConfig.messages_crateKeyCommand_help) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
