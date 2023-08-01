package com.tharox.lobby.kits.selection;

import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.db.DbValues;
import com.tharox.lobby.kits.classes.Kit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class KitListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().contains("PURCHASE KITS"))
            return;
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;

        String name = item.getItemMeta().getDisplayName().substring(17);

        // player clicked a kit class
        if (name.contains("Assassin") || name.contains("Tank")
                || name.contains("Support") || name.contains("Mobility")) return;

        if (!DbValues.getUnlockedKits(player).contains(name)) {
            if (DbValues.getCredits(player) >= Kit.cost) {
                DbValues.addUnlockedKit(player, name);
                DbValues.updateCredits(player, -Kit.cost);

                player.sendMessage(ChatColor.GRAY + "You have purchased the "
                        + HexColor.PURPLE + name + ChatColor.GRAY + " Kit.");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            } else {
                player.sendMessage(Prefixes.getErrorPrefix() + "You do not have enough Credits! Need "
                        + HexColor.RED + (Kit.cost - DbValues.getCredits(player))
                        + ChatColor.RED + " more.");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            }
        } else {
            player.sendMessage(Prefixes.getErrorPrefix() + "You have already unlocked this Kit.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        }

        player.closeInventory();
    }
}
