package net.neverendlesspvp.cratekeys.configs;

import net.neverendlesspvp.cratekeys.config.MeConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class MessageConfig extends MeConfig {

    public static String jsonBuilder_jsonDateFormat="YYYY HH:MM";
    public static String permissions_crateKeyCommand_help ="neverendlesspvp.crates.cratekeycommand.help";
    public static String permissions_crateKeyCommand_base="neverendlesspvp.crates.cratekeycommand";
    public static String permissions_crateKeyCommand_giveAll="neverendlesspvp.crates.cratekeycommand.giveall";
    public static String messages_crateKeyCommand_giveAll_invalidKey = "&cThe key %key% doesn't exist.";

    public static String messages_crateKeyCommand_noPermission = "&cYou don't have enough permissions.";
    public static List<String> messages_crateKeyCommand_help = Arrays.asList("&7-------------------------------",
            "&aCrate key command help",
            "&a/CrateKey Help &8- &7The help command",
            "&a/CrateKey GiveAll <key> <amount> &8- &7Give all crate keys",
            "&a/CrateKey Give <player> <key> <amount> &8- &7Give player crate key",
            "&7-------------------------------");
    public static String messages_crateKeyCommand_giveAll_invalidAmount="&cThe amount %amount% is invalid.";
    public static int settings_crateKeyCommand_giveAll_maxAmount=64;
    public static String messages_crateKeyCommand_giveAll_giveAll="&aGave %amount% %key% keys to %playerCount% players.";

    public MessageConfig() {
        super("messages");
    }
}
