package com.tharox.lobby.listeners;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatListener implements Listener {
    // contains the UUID of players who are in combat
    public static HashMap<UUID, Integer> combatList = new HashMap<>();

    public CombatListener() {
        new BukkitRunnable() {
            @Override
            public void run() {
                onInterval();
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    /**
     * For players only.
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Projectile projectile; // includes all types of arrows and fireworks
        ProjectileSource shooter;
        Player damager;
        Player target;

        if (event.getEntity() instanceof Player)
            target = (Player) event.getEntity();
        else {
            return;
        }

        if (event.getDamager() instanceof Projectile) {
            projectile = (Projectile) event.getDamager();
            shooter = projectile.getShooter();

            if (shooter instanceof Player) {
                damager = (Player) shooter;
            } else return;
        } else if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else return;

        // Damager is at spawn, do nothing.
        if (damager.getLocation().getY() > 210) return;

        // If the player damaged himself/herself, do nothing.
        if (target == damager) return;

        combatList.put(target.getUniqueId(), 20);
        combatList.put(damager.getUniqueId(), 20);
    }

    public void onInterval() {
        HashMap<UUID, Integer> temp = new HashMap<>();

        try {
            for (UUID id : combatList.keySet()) {
                Player player = Bukkit.getPlayer(id);
                if (player == null) continue;

                if (player.isDead()) {
                    temp.remove(id);
                    continue;
                }

                int timer = combatList.get(id) - 1;

                if (timer > 0) {
                    temp.put(id, timer);
                } else {
                    temp.remove(id);

                    player.sendMessage(Prefixes.getImportantPrefix() + HexColor.GREEN + "You are no longer in combat.");
                }
            }
        } catch (ConcurrentModificationException exception) {
            // do nothing
        }

        combatList = temp;
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event) {
        if (combatList.containsKey(event.getPlayer().getUniqueId())) {
            List<String> commands = Arrays.asList("/spawn");

            String[] parts = event.getMessage().split(" ");
            String cmd = parts[0].toLowerCase();

            if (commands.contains(cmd)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Prefixes.getErrorPrefix()
                        + ChatColor.RED + "This command is blocked while in combat.");
            }
        }
    }
}
