package com.tharox.lobby.items;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryOpenListener implements Listener {

    /**
     * Put one stack of lapis into the enchanting table.
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof EnchantingInventory) {
            EnchantingInventory inv = (EnchantingInventory) event.getInventory();
            inv.setSecondary(new ItemStack(Material.LAPIS_LAZULI, 64));
        }
    }
}
