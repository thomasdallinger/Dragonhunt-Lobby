package com.tharox.lobby.listeners;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.db.DatabaseExecutor;
import com.tharox.lobby.game.GameState;
import com.tharox.lobby.movement.ElytraSwapRunnable;
import com.tharox.lobby.scoreboard.Scoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(Main.getSpawnLocation());
        player.setBedSpawnLocation(Main.getSpawnLocation(), true);

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
        event.setJoinMessage(Prefixes.getJoinPrefix() + ChatColor.WHITE + player.getName()
                + ChatColor.GRAY + " has joined.");

        ElytraSwapRunnable.playerWasInAir.put(player, false);

        // Every time a player joins, the scoreboard of every player is updated.
        for (Player target : Bukkit.getOnlinePlayers()) {
            new Scoreboard(target);
        }

        // Sets tablist header and footer.
        Main.getInstance().getTablistManager().setPlayerList(player);
        Main.getInstance().getTablistManager().setAllPlayerTeams();

        GameState.giveItem(player, new ItemStack(Material.CLOCK), HexColor.YELLOW + "Profile"
                + ChatColor.GRAY + " (Right-Click)", 6);
        GameState.giveItem(player, new ItemStack(Material.NETHER_STAR), HexColor.YELLOW + "Purchase Kits"
                + ChatColor.GRAY + " (Right-Click)", 7);
        GameState.giveItem(player, new ItemStack(Material.END_CRYSTAL), HexColor.YELLOW + "Play"
                + ChatColor.GRAY + " (Right-Click)", 8);

        addPlayerToDatabase(player);
    }

    private void addPlayerToDatabase(Player player) {
        AtomicBoolean returningPlayer = new AtomicBoolean(false);

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            Pattern pattern = Pattern.compile("^[\\sa-zA-ZäöüßÄÖÜ0-9!._-]+$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(player.getName());
            if (!matcher.matches()) {
                player.kickPlayer(Prefixes.getErrorPrefix() + "Please change your Playername!");
                throw new RuntimeException("Playername would cause SQL-Injection!");
            }

            String query = "SELECT uuid FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                returningPlayer.set(true);
                player.sendMessage(HexColor.PURPLE.toString() + "Welcome back, " + player.getName() + "!");

                // Update the players name each time the player joins the server.
                query = "UPDATE players SET name = '" + player.getName() + "' WHERE uuid = '"
                        + player.getUniqueId() + "'";

                stmt = con.createStatement();
                stmt.executeQuery(query);

                rs.close();
                stmt.close();
                return;
            }

            query = "INSERT INTO players (uuid, name, first_joined) VALUES ('" + player.getUniqueId()
                    + "', '" + player.getName() + "', '" + Date.valueOf(LocalDate.now()) + "')";

            stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        });

        if (returningPlayer.get()) return;

        player.sendMessage(HexColor.PURPLE + "Welcome to Dragonhunt, " + player.getName() + "!\n"
                + ChatColor.GRAY + "Dive into an " + ChatColor.WHITE + "innovative and action-packed PvPvE gamemode"
                + ChatColor.GRAY + ".\nJoin a match by clicking the " + ChatColor.WHITE + "End Crystal"
                + ChatColor.GRAY + " in your inventory.\n"
                + ChatColor.GRAY + "Don't hesitate to ask fellow players for help. \n"
                + ChatColor.WHITE + " Good luck and enjoy!");

        // Start-Equipment when first joining the server
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        player.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.BOW));
        player.getInventory().setItem(2, new ItemStack(Material.ARROW, 24));

        // Update scoreboard (contains default values after the update)
        new Scoreboard(player);
    }
}
