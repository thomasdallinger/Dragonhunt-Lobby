package com.tharox.lobby.commands;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.game.GameState;
import com.tharox.lobby.items.LootItem;
import com.tharox.lobby.items.LootManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LootCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else return false;

        if (args.length != 1) {
            player.sendMessage(Prefixes.getErrorPrefix() + "Incorrect argument length.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        Inventory inventory = Bukkit.createInventory(null, 54,
                HexColor.PURPLE.toString() + ChatColor.BOLD + "TIER " + args[0]
                        + ChatColor.DARK_GRAY + " Loot Table");

        int tier;

        try {
            tier = Integer.parseInt(args[0]);
        } catch (IllegalArgumentException exception) {
            player.sendMessage(Prefixes.getErrorPrefix() + "Incorrect argument type (needs to be a number).");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        switch (tier) {
            case 1:
                for (LootItem item : LootManager.uncommon) {
                    inventory.addItem(item.make());
                }
                for (LootItem item : LootManager.rare) {
                    inventory.addItem(item.make());
                }
                break;
            case 2:
                for (LootItem item : LootManager.rare) {
                    inventory.addItem(item.make());
                }
                for (LootItem item : LootManager.epic) {
                    inventory.addItem(item.make());
                }
                break;
            case 3:
                for (LootItem item : LootManager.epic) {
                    inventory.addItem(item.make());
                }
                for (LootItem item : LootManager.exotic) {
                    inventory.addItem(item.make());
                }
                break;
            case 4:
                for (LootItem item : LootManager.mythic) {
                    inventory.addItem(item.make());
                }

                inventory.addItem(GameState.createGrappler());
                inventory.addItem(GameState.createKnockbackStick());
                break;
        }

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);

        return true;
    }
}