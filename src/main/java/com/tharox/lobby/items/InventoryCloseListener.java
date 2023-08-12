package com.tharox.lobby.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    /**
     * Replace lapis with air so the player's inventory won't get filled up with lapis.
     * @param event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof EnchantingInventory) {
            EnchantingInventory inv = (EnchantingInventory) event.getInventory();
            inv.setSecondary(new ItemStack(Material.AIR));
        }
    }
}