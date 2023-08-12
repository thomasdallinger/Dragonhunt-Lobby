package com.tharox.lobby.listeners;

import com.tharox.lobby.game.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item;

        if (event.getAction().name().contains("HOTBAR") && event.getHotbarButton() != -1) {
            item = event.getView().getBottomInventory().getItem(event.getHotbarButton());
        } else {
            item = event.getCurrentItem();
        }

        // Make certain items unmovable.

        // 6 = Profile, 7 = Purchase Kits, 8 = Play
        if (event.getHotbarButton() == 6 || event.getHotbarButton() == 7 || event.getHotbarButton() == 8) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        if (event.getSlot() == 6 || event.getSlot() == 7 || event.getSlot() == 8) {
            event.setCancelled(true);
        }

        if (item == null) return;

        // Check if the item was an Elytra and if the Chestplate slot was clicked.
        if (item.getType().equals(Material.ELYTRA)) {
            if (event.getSlot() == 38) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * No item can be dropped.
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        ItemStack offHandItem = event.getOffHandItem();

        if (offHandItem == null) return;

        if (GameState.protectedItemMaterials.contains(offHandItem.getType())) {
            event.setCancelled(true);
        }

        // Elytra cannot be swapped: not in the hotbar, no checking needed
    }
}
