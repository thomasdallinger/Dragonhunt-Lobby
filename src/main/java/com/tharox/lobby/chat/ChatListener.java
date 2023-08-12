package com.tharox.lobby.chat;

import com.tharox.lobby.db.DbValues;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefix = "";

        if (player.isOp()) {
            prefix = Prefixes.getAdminPrefix();
        } else if (DbValues.getRole(player).equals("VIP")) {
            prefix = Prefixes.getVIPPrefix();
        } else if (DbValues.getRole(player).equals("Premium")) {
            prefix = Prefixes.getPremiumPrefix();
        }

        event.setFormat(prefix + ChatColor.WHITE + "%1$s" + Prefixes.getEloSuffix(player)
                + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "%2$s");
    }
}
