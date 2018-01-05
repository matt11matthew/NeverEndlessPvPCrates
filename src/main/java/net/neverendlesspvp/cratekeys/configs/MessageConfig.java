package net.neverendlesspvp.cratekeys.configs;

import net.neverendlesspvp.cratekeys.config.MeConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class MessageConfig extends MeConfig {
    public static String jsonBuilder_jsonDateFormat = "YYYY HH:MM";
    public static String permissions_crateKeyCommand_help = "neverendlesspvp.crates.cratekeycommand.help";
    public static String permissions_crateKeyCommand_base = "neverendlesspvp.crates.cratekeycommand";
    public static String permissions_crateKeyCommand_list = "neverendlesspvp.crates.cratekeycommand.list";
    public static String permissions_crateCommand_base = "neverendlesspvp.crates.cratecommand";
    public static String permissions_crateCommand_delete = "neverendlesspvp.crates.cratecommand.delete";
    public static String permissions_crateCommand_create = "neverendlesspvp.crates.cratecommand.create";
    public static String permissions_crateKeyCommand_giveAll = "neverendlesspvp.crates.cratekeycommand.giveall";
    public static String messages_crateKeyCommand_giveAll_invalidKey = "&cThe key %key% doesn't exist.";
    public static String messages_crateCommand_create_invalidKey = "&cThe key %key% doesn't exist.";
    public static String messages_crateCommand_create = "&cYou've created a crate";
    public static String messages_crateKeyCommand_noPermission = "&cYou don't have enough permissions.";
    public static String messages_crateCommand_noPermission = "&cYou don't have enough permissions.";
    public static List<String> messages_crateKeyCommand_help = Arrays.asList("&7-------------------------------",
            "&aCrate key command help",
            "&a/CrateKey Help &8- &7The help command",
            "&a/CrateKey GiveAll <key> <amount> &8- &7Give all crate keys",
            "&a/CrateKey Give <player> <key> <amount> &8- &7Give player crate key",
            "&7-------------------------------");
    public static String messages_crateKeyCommand_giveAll_invalidAmount = "&cThe amount %amount% is invalid.";
    public static int settings_crateKeyCommand_giveAll_maxAmount = 64;
    public static int settings_cycleTimes = 16;
    public static int settings_cycleWait = 5;
    public static String messages_crateKeyCommand_giveAll_giveAll = "&aGave %amount% %key% keys to %playerCount% players.";
    public static String messages_crateCommand_noCrateExists = "&cNo crate exists here.";
    public static String messages_crateCommand_deleteCrate = "&cDeleted the crate.";
    public static String messages_crateCommand_noCrateAlreadyExists = "&cA crate already exists here";
    public static String crateRewardInventory_title = "%crateName% Rewards";
    public static String crateOpeningInventory_title = "Opening %crateName%...";
    public static int crateOpeningInventory_currentRewardSlot = 13;
    public static String crateRewardInventory_lore = "&aPercent: &7%percent%%";
    public static String messages_notCorrectKey = "&cYou must use a %key% key.";
    public static String sounds_crateTickSound_sound= "NOTE_PLING";
    public static double sounds_crateTickSound_volume= 1.0;
    public static double sounds_crateTickSound_pitch = 1.0;
    public static String hologram_crateLine="%color%%crateName%";

    public MessageConfig() {
        super("messages");
    }
}