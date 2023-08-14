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
    public static final String version = "1.0.6";
    public static final String mode = "Classic";
    public static final boolean eloActive = false;
    public static final String freeKit = "Demolitionist";

    public static final int levelsOnKill = 4;
    public static final int levelsOnLooting = 2;
    public static final int levelsOnDisenchanting = 1;

    public static List<Material> protectedItemMaterials =
            Arrays.asList(Material.CLOCK, Material.NETHER_STAR, Material.END_CRYSTAL);

    public static void givePlayerStartEquipment(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        player.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.BOW));

        GameState.giveItem(player, new ItemStack(Material.SHIELD), ChatColor.AQUA + "Shield", 40);

        GameState.giveItem(player, new ItemStack(Material.CLOCK), HexColor.YELLOW + "Profile"
                + ChatColor.GRAY + " (Right-Click)", 6);
        GameState.giveItem(player, new ItemStack(Material.NETHER_STAR), HexColor.YELLOW + "Purchase Kits"
                + ChatColor.GRAY + " (Right-Click)", 7);
        GameState.giveItem(player, new ItemStack(Material.END_CRYSTAL), HexColor.YELLOW + "Play"
                + ChatColor.GRAY + " (Right-Click)", 8);
    }

    public static void giveItem(Player player, ItemStack item, String title, int index) {
        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(title);

        if (item.getType() == Material.END_CRYSTAL) {
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
