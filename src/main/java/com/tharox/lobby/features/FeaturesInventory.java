package com.tharox.lobby.features;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.db.DbValues;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FeaturesInventory {
    public Inventory menu, color;

    public FeaturesInventory(Player player) {
        menu = createMenuInventory(player);
        color = createColorInventory(player);
    }

    private Inventory createMenuInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, HexColor.PURPLE.toString() + ChatColor.BOLD
                + "FEATURES");

        // Player has premium and has not claimed the credits this day

        // 50 Credits per day, 100 Credits per day for Premium players
        // claimed_credits DATETIME, color VARCHAR(7)
        if (DbValues.getRole(player).equals("Premium")) {
            addItem(inv, Material.GOLD_INGOT, HexColor.YELLOW.toString() + ChatColor.BOLD
                    + "Claim Daily Credits", 11);
        } else {
            addItem(inv, Material.BARRIER, ChatColor.GRAY.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH
                    + "Claim Daily Credits", 11);
        }

        addItem(inv, Material.CYAN_DYE, HexColor.YELLOW.toString() + ChatColor.BOLD + "Custom Color", 13);

        if (DbValues.getCredits(player) >= 3000) {
            //TODO: add lore with Arrays.asList -> how many Credits needed
            addItem(inv, Material.LAVA_BUCKET, HexColor.YELLOW.toString() + ChatColor.BOLD
                    + "Reset Stats", 15);
        } else {
            addItem(inv, Material.BARRIER, ChatColor.GRAY.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH
                    + "Reset Stats", 15);
        }

        return inv;
    }

    private Inventory createColorInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, HexColor.PURPLE.toString() + ChatColor.BOLD
                + "FEATURES " + ChatColor.DARK_GRAY + "Custom Color");

        addItem(inv, Material.WHITE_WOOL, HexColor.GREEN + "Color Preview", 10);

        for (int i = 1; i <= 6; i++) {
            addItem(inv, Material.POLISHED_BLACKSTONE_BUTTON, ChatColor.WHITE + "" + i + " UP", i + 1);
            addItem(inv, Material.BLACK_STAINED_GLASS_PANE, ChatColor.WHITE + "F", i + 10);
            addItem(inv, Material.POLISHED_BLACKSTONE_BUTTON, ChatColor.WHITE + "" + i + " DOWN", i + 19);
        }

        addItem(inv, Material.LIME_STAINED_GLASS_PANE, HexColor.GREEN + "Save Changes", 34);
        addItem(inv, Material.RED_STAINED_GLASS_PANE, HexColor.RED + "Back to Menu", 35);

        return inv;
    }

    private void addItem(Inventory inventory, Material material, String title, int index) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(title);
        item.setItemMeta(meta);

        inventory.setItem(index, item);
    }
}
