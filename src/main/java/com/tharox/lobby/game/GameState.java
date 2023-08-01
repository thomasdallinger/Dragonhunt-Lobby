package com.tharox.lobby.game;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.listeners.GrappleListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState {
    public static void giveItem(Player player, ItemStack item, String title, int index) {
        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(title);

        if (title.equals(HexColor.YELLOW + "Play")) {
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Right-Click: " + HexColor.GREEN + "Join the Queue.",
                    ChatColor.GRAY + "Right-Click again: " + HexColor.RED + "Leave the Queue."));
        }

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);

        player.getInventory().setItem(index, item);
    }

    public static ItemStack createGrappler() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Pull yourself to different locations");
        lore.add(ChatColor.GRAY + "with your Grappling Hook.");
        lore.add(ChatColor.GRAY + "Arrows shot cannot do damage.");
        lore.add(ChatColor.GRAY + "Max. Arrow Distance: ");
        lore.add(ChatColor.WHITE.toString() + GrappleListener.maxArrowDistance + " Blocks");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n");
        lore.add(ChatColor.GRAY.toString() + ChatColor.WHITE
                + (GrappleListener.cooldown / 20) + ChatColor.GRAY + " seconds");

        ItemStack grappler = new ItemStack(Material.CROSSBOW);
        grappler.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        CrossbowMeta meta = (CrossbowMeta) grappler.getItemMeta();

        if (meta == null) return null;

        meta.setDisplayName(HexColor.PURPLE.toString() + ChatColor.MAGIC + "!;"
                + HexColor.PURPLE + " Grappler " + ChatColor.MAGIC + "!;");
        meta.setLore(lore);
        meta.addChargedProjectile(new ItemStack(Material.ARROW, 1));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        grappler.setItemMeta(meta);

        return grappler;
    }
}
