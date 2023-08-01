package com.tharox.lobby.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            event.setFormat(Prefixes.getAdminPrefix() + ChatColor.WHITE + "%1$s" + Prefixes.getEloSuffix(player)
                    + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "%2$s");
        } else {
            event.setFormat(ChatColor.WHITE + "%1$s" + Prefixes.getEloSuffix(player)
                    + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "%2$s");
        }
    }
}
