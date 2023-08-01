package com.tharox.lobby.entities;

import com.tharox.lobby.chat.HexColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Projectile projectile = null; // includes normal Arrows, Tipped Arrows and Spectral Arrows
        ProjectileSource shooter;
        Player damager;
        ItemStack itemInMainHand; // Grappler can never be in a players Offhand

        if (event.getDamager() instanceof Projectile) {
            projectile = (Projectile) event.getDamager();
            shooter = projectile.getShooter();

            if (shooter instanceof Player) {
                damager = (Player) shooter;
            } else return;
        } else if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else return;

        itemInMainHand = damager.getInventory().getItemInMainHand();

        // Grappler cannot do damage
        if (projectile != null) {
            if (itemInMainHand.getItemMeta() == null) return;
            if (itemInMainHand.getItemMeta().getDisplayName().equals(HexColor.PURPLE.toString() + ChatColor.MAGIC + "!;"
                    + HexColor.PURPLE + " Grappler " + ChatColor.MAGIC + "!;")) {
                projectile.remove();
                event.setCancelled(true);
            }
        }

        // Players cannot do damage at spawn
        if (damager.getLocation().getY() > 210) {
            event.setCancelled(true);
        }
    }
}
