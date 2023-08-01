package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.kits.selection.KitListener;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;

public abstract class Kit {
    public static final String title = null;
    public static final Material material = null;
    public static final String lore = null;
    public static final int slot = 0;
    public static final int cost = 3000;

    public static ItemStack getDisplayItem(String title, Material material, String lore) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(title);
        meta.setLore(Arrays.asList(lore.split("\n")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Used for the last line in a Kits lore (not used for classes).
     */
    public static ItemStack getDisplayItem(String title, Material material, String lore, boolean unlocked) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(title);

        if (unlocked) {
            lore = lore + HexColor.GREEN + ChatColor.BOLD + "ᴜɴʟᴏᴄᴋᴇᴅ";
        } else {
            lore = lore + HexColor.YELLOW + ChatColor.BOLD + "ᴄᴏꜱᴛ " + cost + HexColor.YELLOW + " ⛃";
        }

        meta.setLore(Arrays.asList(lore.split("\n")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Includes PotionMeta and Color for Potions, Splash Potions, Lingering Potions and Tipped Arrows.
     */
    public static ItemStack getDisplayItem(String title, Material material, String lore, Color color) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(title);
        meta.setLore(Arrays.asList(lore.split("\n")));
        meta.setColor(color);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }
}
