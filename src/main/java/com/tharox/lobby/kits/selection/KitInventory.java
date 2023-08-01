package com.tharox.lobby.kits.selection;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.db.DbValues;
import com.tharox.lobby.kits.classes.DragonhuntKit;
import com.tharox.lobby.kits.classes.Kit;
import com.tharox.lobby.kits.classes.KitClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.reflections.Reflections;

import java.util.Set;

public class KitInventory {
    public Inventory kits;

    public KitInventory(Player player) {
        createInventory(player);
    }

    public void createInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, HexColor.PURPLE.toString() + ChatColor.BOLD
                + "PURCHASE KITS "
                + ChatColor.DARK_GRAY + DbValues.getCredits(player) + " ⛃");

        // Set the kit classes
        inv.setItem(0, Kit.getDisplayItem(HexColor.ASSASSIN.toString() + ChatColor.BOLD
                        + KitClass.ASSASSIN + " Assassin" + ChatColor.GRAY + " (Class)",
                Material.NETHERITE_SWORD,
                ChatColor.DARK_GRAY + "ᴘᴀꜱꜱɪᴠᴇ\n"
                        + ChatColor.WHITE + "+25%" + ChatColor.GRAY + " Damage"));

        inv.setItem(9, Kit.getDisplayItem(HexColor.TANK.toString() + ChatColor.BOLD
                        + KitClass.TANK + " Tank" + ChatColor.GRAY + " (Class)",
                Material.NETHERITE_CHESTPLATE,
                ChatColor.DARK_GRAY + "ᴘᴀꜱꜱɪᴠᴇ\n"
                        + ChatColor.WHITE + "+2 " + ChatColor.GRAY + "❤"));

        inv.setItem(18, Kit.getDisplayItem(HexColor.SUPPORT.toString() + ChatColor.BOLD
                        + KitClass.SUPPORT + " Support" + ChatColor.GRAY + " (Class)",
                Material.POTION,
                ChatColor.DARK_GRAY + "ᴘᴀꜱꜱɪᴠᴇ\n"
                        + ChatColor.WHITE + "+20%" + ChatColor.GRAY + " Healing",
                Color.fromRGB(0x1F1F23)));

        inv.setItem(27, Kit.getDisplayItem(HexColor.MOBILITY.toString() + ChatColor.BOLD
                        + KitClass.MOBILITY + " Mobility" + ChatColor.GRAY + " (Class)",
                Material.NETHERITE_BOOTS,
                ChatColor.DARK_GRAY + "ᴘᴀꜱꜱɪᴠᴇ\n"
                        + ChatColor.WHITE + "+20%" + ChatColor.GRAY + " Attack Speed,\n"
                        + ChatColor.WHITE + "+40%" + ChatColor.GRAY + " Mining Speed"));

        // Add the kits
        Reflections reflections = new Reflections("com.tharox.lobby");
        Set<Class<?>> kitClasses = reflections.getTypesAnnotatedWith(DragonhuntKit.class);

        kitClasses.forEach(
                (kitClass) -> {
                    if (Kit.class.isAssignableFrom(kitClass)) {
                        try {
                            // Kit unlocked
                            // IMPORTANT: substring(17) needed to remove HexColor + Symbol
                            if (DbValues.getUnlockedKits(player).contains(
                                    ((String) kitClass.getField("title").get(null)).substring(17))
                                    || DbValues.getRole(player).equals("Premium")
                                    || DbValues.getRole(player).equals("VIP")
                                    || player.isOp()) {
                                inv.setItem((Integer) kitClass.getField("slot").get(null),
                                        Kit.getDisplayItem((String) kitClass.getField("title").get(null),
                                                (Material) kitClass.getField("material").get(null),
                                                (String) kitClass.getField("lore").get(null),
                                                true));
                            }
                            // Kit not unlocked: Material Barrier Block
                            else {
                                inv.setItem((Integer) kitClass.getField("slot").get(null),
                                        Kit.getDisplayItem((String) kitClass.getField("title").get(null),
                                                Material.BARRIER,
                                                (String) kitClass.getField("lore").get(null),
                                                false));
                            }

                            // get(null) because static
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        kits = inv;
    }
}
