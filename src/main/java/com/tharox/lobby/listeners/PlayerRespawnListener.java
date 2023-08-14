package com.tharox.lobby.listeners;

import com.tharox.lobby.Main;
import com.tharox.lobby.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(),
                task -> GameState.givePlayerStartEquipment(player), 10L);
    }
}