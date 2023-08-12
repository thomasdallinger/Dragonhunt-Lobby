package com.tharox.lobby.listeners;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.db.DatabaseExecutor;
import com.tharox.lobby.db.DbValues;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Statement;

public class PlayerDeathListener implements Listener {

    /**
     * When a player dies, the player looses the entire inventory and all XP.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();

        // Death message
        if (event.getDeathMessage() == null) return;

        addPlayerDeath(player);

        // strike a lightning effect at the player's death location
        player.getWorld().strikeLightningEffect(player.getLocation());

        event.setDeathMessage(Prefixes.getDeathPrefix()
                + HexColor.RED + event.getDeathMessage().substring(0, player.getName().length())
                + ChatColor.GRAY + event.getDeathMessage().substring(player.getName().length()));

        if (killer != null) {
            CombatListener.combatList.remove(killer.getUniqueId());
            killer.sendMessage(Prefixes.getImportantPrefix() + HexColor.GREEN + "You are no longer in combat.");

            // Player did not kill himself/herself
            if (!killer.equals(player)) {
                int levelsToAdd = 5;
                killer.setLevel(killer.getLevel() + levelsToAdd);
                killer.sendTitle("", HexColor.GREEN + "+" + levelsToAdd + ChatColor.WHITE + " Level",
                        0, 30, 10);
                killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                killer.sendMessage(ChatColor.GRAY + "You have killed "
                        + ChatColor.WHITE + player.getName() + ChatColor.GRAY + ".");

                killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                killer.sendMessage(HexColor.GREEN + "You received " + ChatColor.DARK_GREEN + "Regeneration 2"
                        + HexColor.GREEN + " for " + ChatColor.DARK_GREEN + "10" + HexColor.GREEN + " seconds.");

                addPlayerKill(killer);

                player.sendMessage(Prefixes.getDeathPrefix() + ChatColor.GRAY + "You have been killed by "
                        + HexColor.RED + killer.getName()
                        + ChatColor.WHITE + " (" + String.format("%1.1f", (killer.getHealth() / 2)) + " ❤" + ")"
                        + ChatColor.GRAY + ".");

                for (ItemStack item : event.getDrops()) {
                    if (item.getItemMeta() == null) continue;
                    // Do not drop non-SkyPvP items.
                    if (!item.getItemMeta().getDisplayName().equals(HexColor.YELLOW + "Profile")
                            && !item.getItemMeta().getDisplayName().equals(HexColor.YELLOW + "Purchase Kits")
                            && !item.getItemMeta().getDisplayName().equals(HexColor.YELLOW + "Play")) {
                        killer.getWorld().dropItemNaturally(killer.getLocation(), item);
                    }
                }
            } else {
                // Do not drop anything: the player died by him/her-self
                event.getDrops().clear();
            }
        }

        // Set dead player out of combat
        CombatListener.combatList.remove(player.getUniqueId());

        event.setKeepLevel(true);
        player.setLevel(0);
    }

    private void addPlayerDeath(Player player) {
        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String query = "UPDATE players SET skypvp_deaths = '" + (DbValues.getSkyPvPKills(player, false) + 1)
                    + "' WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        });
    }

    private void addPlayerKill(Player killer) {
        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String query = "UPDATE players SET skypvp_kills = '" + (DbValues.getSkyPvPKills(killer, true) + 1)
                    + "' WHERE uuid = '" + killer.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        });
    }
}
