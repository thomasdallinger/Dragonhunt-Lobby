package com.tharox.lobby.scoreboard;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.db.DbValues;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Scoreboard extends ScoreboardBuilder {
    public static int maxBeacons = 30;

    public Scoreboard(Player player) {
        super(player, Main.getTitle());
        update(player);
    }

    @Override
    public void createScoreboard() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();

        setScore(ChatColor.GRAY + dtf.format(now) + ChatColor.DARK_GRAY + " [L]", 13);
        setScore("", 12);
        setScore("", 11);
        setScore("", 10);
        setScore("", 9);
        setScore("", 8);
        setScore("", 7);
        setScore("", 6);
        setScore("", 5);
        setScore("", 4);
        setScore("", 3);
        setScore("", 2);
        setScore("", 1);
        setScore("", 0);
    }

    @Override
    public void update(Player player) {
        // Penalty Points: 1 / 12 --> 12 = ban in this example

        if (DbValues.getSkyPvPKills(player, false) > 0) { // would be a division by 0
            setScore("SkyPvP:" + " ☠ " + DbValues.getSkyPvPKills(player, true)
                    + " ✟ " + DbValues.getSkyPvPKills(player, false) + " "
                    + ChatColor.GRAY + "(" + roundDecimalNumber(((double) DbValues.getSkyPvPKills(player, true)
                    / DbValues.getSkyPvPKills(player, false)), 2) + ")", 12);
        } else {
            setScore("SkyPvP:" + " ☠ " + DbValues.getSkyPvPKills(player, true)
                    + " ✟ " + DbValues.getSkyPvPKills(player, false) + " "
                    + ChatColor.GRAY + "(" + DbValues.getSkyPvPKills(player, true) + ")", 12);
        }

        setScore("Rank: " + ChatColor.GOLD + DbValues.getRank(player)
                + ChatColor.GRAY + " (" + DbValues.getElo(player) + " Elo)", 10);

        setScore("League: " + ChatColor.GRAY + ChatColor.BOLD + "ꜱɪʟᴠᴇʀ", 9);

        setScore("Credits: " + ChatColor.GOLD + DbValues.getCredits(player) + " ⛃", 8);

        if (DbValues.getKills(player, false) > 0) { // would be a division by 0
            setScore("Kills: " + HexColor.YELLOW + DbValues.getKills(player, true)
                    + ChatColor.GRAY + " (K/D: " + roundDecimalNumber(((double) DbValues.getKills(player, true)
                    / DbValues.getKills(player, false)), 2) + ")", 6);
        } else {
            setScore("Kills: " + HexColor.YELLOW + DbValues.getKills(player, true)
                    + ChatColor.GRAY + " (K/D: " + DbValues.getKills(player, true) + ")", 6);
        }

        setScore("Deaths: " + HexColor.YELLOW + DbValues.getKills(player, false), 5);
        setScore("Games: " + HexColor.YELLOW + DbValues.getGamesPlayed(player), 4);

        if (DbValues.getAvgPosition(player) == 0) {
            setScore("Avg. Position: " + HexColor.YELLOW
                    + roundDecimalNumber(DbValues.getAvgPosition(player), 2), 3);
        } else {
            setScore("Avg. Position: " + HexColor.YELLOW + "-", 3);
        }

        setScore("Wins: " + HexColor.YELLOW + DbValues.getWins(player), 2);

        if (DbValues.getGamesPlayed(player) != 0) { // would be a division by 0
            setScore("Win Rate: " + HexColor.YELLOW
                    + roundDecimalNumber(((double) DbValues.getWins(player)
                    / DbValues.getGamesPlayed(player)) * 100, 2) + "%", 1);
        } else {
            setScore("Win Rate: " + HexColor.YELLOW + "0%", 1);
        }

        if (DbValues.getWins(player) > 0) { // check if the player has won a game
            setScore("Fastest: "
                    + HexColor.YELLOW + DbValues.getFastestWin(player), 0);
        } else {
            setScore("Fastest: " + HexColor.YELLOW + "--:--", 0);
        }
    }

    /**
     * Rounds the passed value to the number of passed decimal places.
     *
     * @param value         is the value to be rounded.
     * @param decimalPoints is the number of decimal places to round to.
     */
    private double roundDecimalNumber(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }
}
