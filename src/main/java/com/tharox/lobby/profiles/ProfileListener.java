package com.tharox.lobby.profiles;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ProfileListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ProfileInventory premiumInventory = new ProfileInventory(player);

        if (!event.getView().getTitle().contains("MANAGE PROFILE")) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;

        String clickedItemName = item.getItemMeta().getDisplayName();

        // inventory is the manage profile menu
        if (event.getView().getTitle().equals("MANAGE PROFILE MENU")) {
            if (clickedItemName.contains("Custom Color")) {
                player.closeInventory();
                player.openInventory(premiumInventory.color);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            } else {
                player.sendMessage(Prefixes.getErrorPrefix() + "Coming soon...");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            }

            return;
        }

        // event.getInventory() ... top inventory = manage profile
        // if the clicked inventory is not the profile inventory, cancel the event
        if (event.getClickedInventory() != event.getInventory()) {
            event.setCancelled(true);
            return;
        }

        if (event.getClick() == ClickType.NUMBER_KEY) return;

        // back to menu functionality
        if (clickedItemName.equals(HexColor.RED + "Back to Menu")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
            player.closeInventory();
            player.openInventory(premiumInventory.menu);
            return;
        }

        // NOTE: always refresh the inventory instance before opening it again
        // so all data (Locked / Unlocked / Credits) updates
        premiumInventory = new ProfileInventory(player);
        player.openInventory(premiumInventory.color);
    }
}
