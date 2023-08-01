package com.tharox.lobby.queue;

import com.tharox.lobby.game.GameStart;
import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.db.DatabaseExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Queue {
    public static int serverID;
    // only 1 queue at a time
    public static List<Player> inQueue = new ArrayList<>();
    public static int time = 121;
    public static BossBar bossBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);

    /**
     * Executed every second (see Main.java).
     */
    public static void onInterval() {
        // CASE 1: More than 0 players in the queue
        if (inQueue.size() > 0) {
            time--;
            addNotificationsToPlayers();

            if (time > 0) {
                bossBar.setProgress(time / 122D);
                bossBar.setTitle(ChatColor.WHITE + "Game starting in "
                        + HexColor.GREEN + ChatColor.BOLD + timeFormat()
                        + ChatColor.WHITE + " (" + Queue.inQueue.size() + " / 8)");
                bossBar.setColor(BarColor.GREEN);
            }

            // start the node
            if (time == 78) Main.setServerStatus("start", serverID);
            // move players to the node (using its serverID)
            if (time == 0) {
                // set the time very high (higher than the time it takes to move players to the node + 120 seconds)
                time = 139;
                new GameStart(serverID, bossBar);
            }

            // CASE 2: 0 players in the queue
        } /*else if (inQueue.size() == 0) {
            time = 121;
            addNotificationsToPlayers();

            bossBar.setProgress(1.0);
            bossBar.setTitle(ChatColor.WHITE + "Waiting for 1 more Player!");
            bossBar.setColor(BarColor.RED);

        // CASE 3: No players are in the queue
        }*/ else {
            time = 121;
            checkServerRunning();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (bossBar.getPlayers().contains(player)) {
                    bossBar.removePlayer(player);
                }
            }
        }
    }

    /**
     * @return the time format: mm:ss
     */
    public static String timeFormat() {
        String minutes = "";
        String seconds = "";

        if ((time / 60) % 60 < 10)
            minutes = "0";
        minutes += (time / 60) % 60;
        if (time % 60 < 10)
            seconds = "0";
        seconds += time % 60;

        return minutes + ":" + seconds;
    }

    /**
     * Checks if any of the nodes are currently running.
     * Gets the ID of the first server that isn't running.
     * Sets the ServerID to 0 if all servers are running, which means that no queue can be started.
     */
    private static void checkServerRunning() {
        DatabaseExecutor.executeDatabaseHandler((con) -> {
            for (int id = 1; id <= 3; id++) { // 3 servers available
                String query = "SELECT is_running FROM servers WHERE id = '" + id + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                int columns = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= columns; i++) {
                        if (!rs.getBoolean(i)) {     //get first server that is not running
                            Queue.serverID = id;
                            rs.close();
                            stmt.close();
                            return;
                        } else {
                            Queue.serverID = 0;
                        }
                    }
                }
                rs.close();
                stmt.close();
            }
        });
    }

    /**
     * Sets Boss Bar visibility, sends sounds and messages to players.
     */
    private static void addNotificationsToPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
                bossBar.setVisible(true);
            }

            if (time <= 60 && time % 20 == 0 || time <= 10) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                player.sendMessage(Prefixes.getAllPrefix() + ChatColor.GRAY + "Game starting in "
                        + HexColor.GREEN + time + ChatColor.GRAY + " seconds.");
            }
        }
    }
}
