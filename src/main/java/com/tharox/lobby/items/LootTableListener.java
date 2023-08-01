package com.tharox.lobby.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LootTableListener implements Listener {

    /**
     * Players are not able to get an item from the loot table display.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Loot Table")) {
            event.setCancelled(true);
        }
    }
}
