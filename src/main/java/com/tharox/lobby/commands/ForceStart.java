package com.tharox.lobby.commands;

import com.tharox.lobby.queue.Queue;
import com.tharox.lobby.chat.Prefixes;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else return false;

        if (!player.isOp()) {
            player.sendMessage(Prefixes.getErrorPrefix() + "You do not have permission to execute this command!");
            return false;
        }

        if (!Queue.inQueue.contains(player)) {
            player.sendMessage(Prefixes.getErrorPrefix() + "You are not in a queue.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        if (Queue.time < 80) {
            player.sendMessage(Prefixes.getErrorPrefix() + "The game starts in less than 80 seconds.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        Queue.time = 80;

        return true;
    }
}
