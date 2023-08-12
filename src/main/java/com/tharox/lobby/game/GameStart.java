package com.tharox.lobby.game;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.tharox.lobby.Main;
import com.tharox.lobby.queue.Queue;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.db.DatabaseExecutor;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameStart {
    public int serverID;
    public BossBar bossBarTime;

    public GameStart(int serverID, BossBar bossBarTime) {
        this.serverID = serverID;
        this.bossBarTime = bossBarTime;

        addDatabaseValues();
        movePlayers();
    }

    /**
     * Creates a new game and determines which players are playing it.
     */
    private void addDatabaseValues() {
        DatabaseExecutor.executeDatabaseHandler((con) -> {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String query = "INSERT INTO games (server_id, date, version, mode, elo_active, free_kit) " +
                    "VALUES ('" + serverID + "', '" + dtf.format(now) + "', '" + GameState.version
                    + "', '" + GameState.mode + "', '" + GameState.eloActive + "', '" + GameState.freeKit + "')";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();

            query = "SELECT MAX(id) FROM games";

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int columns = rs.getMetaData().getColumnCount();
            int gameID = 0;

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    gameID = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();

            for (Player player : Queue.inQueue) {
                query = "INSERT INTO plays (game_id, player) " +
                        "VALUES ('" + gameID + "', '" + player.getUniqueId() + "')";

                stmt = con.createStatement();
                stmt.execute(query);
                stmt.close();
            }
        });
    }

    /**
     * Moves all players that are in the queue to the started node.
     * Removes players from the queue afterwards.
     */
    private void movePlayers() {
        for (Player player : Queue.inQueue) {
            bossBarTime.removePlayer(player);
            player.sendMessage(Prefixes.getAllPrefix() + HexColor.GREEN
                    + "Moving players... (Server: " + serverID + ")");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }

        new BukkitRunnable() {
            int duration = 15; // move all players, who where in the queue, within 15 seconds
            Player playerToMove;

            @Override
            public void run() {
                if (duration <= 0) {
                    this.cancel();
                    updateServerStatus();
                }

                // move one player at every odd second
                // max. amount of players: 8
                if (duration % 2 == 1) {
                    if (Queue.inQueue.size() > 0) {
                        playerToMove = Queue.inQueue.get(0);

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF("MhNode" + serverID);
                        playerToMove.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());

                        // remove the player from the Queue
                        // the element with index 0 in the list now has changed
                        // index 0 is now going to be a different player or the list is empty
                        Queue.inQueue.remove(playerToMove);
                    }
                }

                duration -= 1;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    /**
     * Sets the server status to isRunning = true.
     */
    private void updateServerStatus() {
        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "UPDATE servers SET is_running = true WHERE id = '" + serverID + "'";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();

        });
    }
}
