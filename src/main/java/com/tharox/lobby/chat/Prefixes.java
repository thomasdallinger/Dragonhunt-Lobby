package com.tharox.lobby.chat;

import com.tharox.lobby.db.DbValues;
import com.tharox.lobby.scoreboard.Scoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Prefixes {

    // ALL: used for messages displayed to all players
    public static String getAllPrefix() {
        return ChatColor.DARK_GRAY + "[" + HexColor.YELLOW + ChatColor.BOLD + "»»" + ChatColor.DARK_GRAY + "] "
                + ChatColor.GRAY;
    }

    // IMPORTANT: used for important messages
    public static String getImportantPrefix() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "!!!"
                + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    // BEACONS: used for messages related to beacons
    public static String getBeaconPrefix(Player player) {
        return ChatColor.DARK_GRAY + "[" + HexColor.GREEN + DbValues.getBeacons(player)
                + ChatColor.WHITE + "/" + Scoreboard.maxBeacons + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    // ERROR: when a player is not allowed to do something or is doing something wrong
    public static String getErrorPrefix() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "✖✖✖" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;
    }

    // JOIN: when a player joins
    public static String getJoinPrefix() {
        return ChatColor.DARK_GRAY + "[" + HexColor.GREEN + "»»»" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    // LEAVE: when a player leaves
    public static String getLeavePrefix() {
        return ChatColor.DARK_GRAY + "[" + HexColor.RED + "«««" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    // ADMIN: players who have OP
    public static String getAdminPrefix() {
        return ChatColor.DARK_GRAY + "[" + HexColor.RED + "Admin" + ChatColor.DARK_GRAY + "] ";
    }

    // DEATH: when a player dies
    public static String getDeathPrefix() {
        return ChatColor.DARK_GRAY + "[" + HexColor.RED + "☠☠☠" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    }

    public static String getEloSuffix(Player player) {
        return ChatColor.GRAY + " (" + DbValues.getElo(player) + ")";
    }
}
