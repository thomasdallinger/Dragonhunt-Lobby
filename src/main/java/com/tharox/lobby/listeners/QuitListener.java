package com.tharox.lobby.listeners;

import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.queue.Queue;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Queue.inQueue.remove(player);
        event.setQuitMessage(Prefixes.getLeavePrefix() + ChatColor.WHITE + player.getName()
                + ChatColor.GRAY + " has left.");
    }
}
