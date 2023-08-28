package com.tharox.lobby.db;

import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DbValues {
    public static String getRole(Player player) {
        final String[] role = new String[1]; // array size 1: only expecting one value

        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "SELECT role FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    role[0] = rs.getString(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return role[0];
    }

    /**
     * @param kills if true: returns amount of kills, if false: returns amount of deaths
     * @return the amount of kills/deaths the specific player has
     */
    public static int getKills(Player player, boolean kills) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String query = "";
            if (kills)
                query = "SELECT COUNT(*) FROM kills WHERE killer = '" + player.getUniqueId() + "'";
            else
                query = "SELECT COUNT(*) FROM deaths WHERE player = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * @param kills if true: returns amount of kills, if false: returns amount of deaths
     * @return the amount of kills/deaths the specific player has
     */
    public static int getSkyPvPKills(Player player, boolean kills) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String query = "";
            if (kills)
                query = "SELECT skypvp_kills FROM players WHERE uuid = '" + player.getUniqueId() + "'";
            else
                query = "SELECT skypvp_deaths FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * @return the average finishing position of a player
     */
    public static int getAvgPosition(Player player) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            amount[0] = 0;

            String query = "SELECT AVG(position) FROM plays INNER JOIN players"
                    + "ON plays.player = players.uuid WHERE position IS NOT NULL"
                    + "AND plays.player = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * @return the amount of wins the specific player has
     */
    public static int getWins(Player player) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String query = "SELECT COUNT(*) FROM plays WHERE position = 1 " +
                    "AND player = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * @return a list of all players who joined the lobby
     * (player has been added to the player table in the db)
     */
    public static ArrayList<String> getAllPlayers() {
        ArrayList<String> players = new ArrayList<>();

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String query = "SELECT uuid FROM players";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    players.add(rs.getString(i));
                }
            }
            rs.close();
            stmt.close();
        });

        return players;
    }

    /**
     * @return the rank of the specific player
     */
    public static int getRank(Player player) {
        int rank = 0;
        HashMap<String, Integer> unsortedPlayerWins = new HashMap<>();
        String[] playerUUIDs;

        // Put all players who played the game as a key in the HashMap and the corresponding number of wins as a value
        for (String uuid : getAllPlayers()) {
            unsortedPlayerWins.put(uuid, getWins(player));
        }

        // LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> sortedPlayerWins = new LinkedHashMap<>(); // sorted descending

        // Use Comparator.reverseOrder() for reverse ordering
        unsortedPlayerWins.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedPlayerWins.put(x.getKey(), x.getValue()));

        playerUUIDs = sortedPlayerWins.toString().substring(1, sortedPlayerWins.toString().length() - 1)
                .trim().split(",");
        // remove { at the beginning, } at the end and all spaces
        // example: {name=5, name=4, name=3} to players[0]="name=5" players[1]="name=4" players[2]="name=3"

        for (String playerUUID : playerUUIDs) {
            rank++;
            if (playerUUID.contains(player.getUniqueId().toString()))
                break;
        }

        return rank;
    }

    /**
     * @return the elo of the specific player
     */
    public static int getElo(Player player) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "SELECT elo FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * @return the amount of credits of the specific player
     */
    public static int getCredits(Player player) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "SELECT credits FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * if credits > 0: add credits
     * if credits < 0: remove credits
     */
    public static void updateCredits(Player player, int credits) {
        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "UPDATE players SET credits = '" + getCredits(player) + credits
                    + "' WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        });
    }

    /**
     * @return the amount of Beacons the player has found
     */
    public static int getBeacons(Player player) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "SELECT beacons FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * Adds 1 Beacon to the amount of Beacons the player has found.
     */
    public static void addBeacon(Player player) {
        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "UPDATE players SET beacons = '" + (getBeacons(player) + 1)
                    + "' WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        });
    }

    /**
     * @return amount of games the specific player has played
     */
    public static int getGamesPlayed(Player player) {
        final int[] amount = {0};

        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "SELECT COUNT(*) FROM plays WHERE player = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    amount[0] = rs.getInt(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return amount[0];
    }

    /**
     * @return the time of the fastest win of the specific player
     */
    public static String getFastestWin(Player player) {
        final String[] fastestTimestamp = new String[1];

        DatabaseExecutor.executeDatabaseHandler((con) -> {
            String duration;
            int minutes, seconds;

            String query = "SELECT g.duration FROM plays p INNER JOIN games g " +
                    "ON p.game_id = g.id WHERE position = 1 AND player = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            List<Integer> times = new ArrayList<>();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    duration = rs.getString(i);

                    minutes = Integer.parseInt(duration.substring(0, 2));
                    seconds = Integer.parseInt(duration.substring(3));
                    seconds = seconds + (minutes * 60);

                    times.add(seconds);
                }
            }

            rs.close();
            stmt.close();

            Collections.sort(times); // sorted ascending
            int fastestTime = times.get(0); // first element = lowest time
            fastestTimestamp[0] = getTime(fastestTime); // display time as string in mm:ss format
        });

        return fastestTimestamp[0];
    }

    public static String getUnlockedKits(Player player) {
        final String[] kits = new String[1]; // array size 1: only expecting one value

        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "SELECT kits FROM players WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int columns = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    kits[0] = rs.getString(i);
                }
            }
            rs.close();
            stmt.close();
        });

        return kits[0];
    }

    /**
     * String format: "kit;kit[;kitToAdd]" where the part in the brackets is the added part
     */
    public static void addUnlockedKit(Player player, String kitToAdd) {
        DatabaseExecutor.executeDatabaseHandler((con) -> {

            String query = "UPDATE players SET kits = '" + getUnlockedKits(player) + ";"
                    + kitToAdd + "' WHERE uuid = '" + player.getUniqueId() + "'";

            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        });
    }

    public static String getTime(int time) {
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
}
