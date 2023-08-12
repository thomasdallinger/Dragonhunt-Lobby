package com.tharox.lobby.listeners;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.game.GameState;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {
            player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
            player.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
            player.getInventory().setItem(1, new ItemStack(Material.BOW));
            player.getInventory().setItem(2, new ItemStack(Material.ARROW, 24));

            GameState.giveItem(player, new ItemStack(Material.CLOCK), HexColor.YELLOW + "Profile"
                    + ChatColor.GRAY + " (Right-Click)", 6);
            GameState.giveItem(player, new ItemStack(Material.NETHER_STAR), HexColor.YELLOW + "Purchase Kits"
                    + ChatColor.GRAY + " (Right-Click)", 7);
            GameState.giveItem(player, new ItemStack(Material.END_CRYSTAL), HexColor.YELLOW + "Play"
                    + ChatColor.GRAY + " (Right-Click)", 8);
        }, 5L);
    }
}