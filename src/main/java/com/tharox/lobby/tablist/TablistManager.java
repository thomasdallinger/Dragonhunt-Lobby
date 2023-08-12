package com.tharox.lobby.tablist;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.db.DbValues;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistManager {

    /**
     * Sets a custom header and footer in a players tablist.
     *
     * @param player player who receives the header and footer in the tablist
     */
    public void setPlayerList(Player player) {
        player.setPlayerListHeaderFooter(Main.getTitle(), HexColor.YELLOW.toString() + "ᴘʟᴀʏᴅʀᴀɢᴏɴʜᴜɴᴛ.ɴᴇᴛ");
    }

    /**
     * Executed when a player joins.
     */
    public void setAllPlayerTeams() {
        Bukkit.getOnlinePlayers().forEach(this::setPlayerTeams);
    }

    /**
     * One player receives the formatted tablist (using teams) which includes
     * certain stats of all online players.
     *
     * @param player player who receives the formatted tablist
     */
    public void setPlayerTeams(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Team admins = scoreboard.getTeam("0admins");

        if (admins == null)
            admins = scoreboard.registerNewTeam("0admins");

        admins.setPrefix(Prefixes.getAdminPrefix());
        admins.setColor(org.bukkit.ChatColor.WHITE);
        admins.setSuffix(Prefixes.getEloSuffix(player));

        Team vips = scoreboard.getTeam("1vips");

        if (vips == null)
            vips = scoreboard.registerNewTeam("1vips");

        vips.setPrefix(Prefixes.getVIPPrefix());
        vips.setColor(org.bukkit.ChatColor.WHITE);
        vips.setSuffix(Prefixes.getEloSuffix(player));

        Team premiums = scoreboard.getTeam("2premiums");

        if (premiums == null)
            premiums = scoreboard.registerNewTeam("2premiums");

        premiums.setPrefix(Prefixes.getPremiumPrefix());
        premiums.setColor(org.bukkit.ChatColor.WHITE);
        premiums.setSuffix(Prefixes.getEloSuffix(player));

        Team players = scoreboard.getTeam("3players");

        if (players == null)
            players = scoreboard.registerNewTeam("3players");

        players.setColor(org.bukkit.ChatColor.WHITE);
        players.setSuffix(Prefixes.getEloSuffix(player));

        int playerCount = 0;

        // Add players to teams: sorted alphabetically, grouped by team type (admin > vip > premium > player)
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.isOp()) {
                Team team = scoreboard.getTeam("0" + playerCount + "admin");
                if (team == null)
                    team = scoreboard.registerNewTeam("0" + playerCount + "admin");

                team.setPrefix(Prefixes.getAdminPrefix());
                team.setColor(org.bukkit.ChatColor.WHITE);
                team.setSuffix(Prefixes.getEloSuffix(player));
                team.addEntry(target.getName());

            } else if (DbValues.getRole(target).equals("VIP")) {
                Team team = scoreboard.getTeam("1" + playerCount + "vip");
                if (team == null)
                    team = scoreboard.registerNewTeam("1" + playerCount + "vip");

                team.setPrefix(Prefixes.getVIPPrefix());
                team.setColor(org.bukkit.ChatColor.WHITE);
                team.setSuffix(Prefixes.getEloSuffix(player));
                team.addEntry(target.getName());

            } else if (DbValues.getRole(target).equals("Premium")) {
                Team team = scoreboard.getTeam("2" + playerCount + "premium");
                if (team == null)
                    team = scoreboard.registerNewTeam("2" + playerCount + "premium");

                team.setPrefix(Prefixes.getPremiumPrefix());
                team.setColor(org.bukkit.ChatColor.WHITE);
                team.setSuffix(Prefixes.getEloSuffix(player));
                team.addEntry(target.getName());

            } else {
                Team team = scoreboard.getTeam("3" + playerCount + "player");
                if (team == null)
                    team = scoreboard.registerNewTeam("3" + playerCount + "player");

                team.setColor(org.bukkit.ChatColor.WHITE);
                team.setSuffix(Prefixes.getEloSuffix(player));
                team.addEntry(target.getName());
            }

            playerCount++;
        }
    }
}
