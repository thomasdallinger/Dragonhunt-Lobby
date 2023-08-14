package com.tharox.lobby.commands;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.Prefixes;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else return false;

        if (player.getLocation().getY() > 135) {
            player.teleport(Main.getSpawnLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
        } else {
            player.sendMessage(Prefixes.getErrorPrefix() + "Cannot teleport to spawn at your current location.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        }

        return true;
    }
}
