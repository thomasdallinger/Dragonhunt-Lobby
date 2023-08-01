package com.tharox.lobby.commands;

import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.db.DbValues;
import com.tharox.lobby.scoreboard.Scoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BeaconCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        int number;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else return false;

        if (args.length != 2) {
            player.sendMessage(Prefixes.getErrorPrefix() + "You do not have permission to execute this command!");
            return false;
        }

        if (!args[1].equals("REMOVED_BEACON_KEY")) {
            player.sendMessage(Prefixes.getErrorPrefix() + "You do not have permission to execute this command!");
            return false;
        }

        try {
            number = Integer.parseInt(args[0]);
        } catch (Exception e) {
            return false;
        }

        if (DbValues.getBeacons(player) >= number) {
            player.sendMessage(Prefixes.getErrorPrefix() + "You have already found this Beacon.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;

        } else if ((DbValues.getBeacons(player) + 1) == number) {
            DbValues.addBeacon(player);

            // update the scoreboard of the player who clicked the Beacon
            new Scoreboard(player);

            player.sendMessage(Prefixes.getBeaconPrefix(player) + ChatColor.GRAY + "You have found Beacon number "
                    + HexColor.GREEN + DbValues.getBeacons(player) + ChatColor.GRAY + ".");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

            return true;
        } else {
            player.sendMessage(Prefixes.getErrorPrefix()
                    + "You need to find the Beacons in order (look at the numbers).");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }
    }
}
