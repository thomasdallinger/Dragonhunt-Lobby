package com.tharox.lobby.arrows;

import com.tharox.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Arrows {
    /**
     * Arrow generation is started once the server starts.
     */
    public Arrows() {
        generateArrows();
    }

    public void generateArrows() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack arrowStack = player.getInventory().getItem(17);
                    ItemStack generatedArrows;

                    if (arrowStack == null
                            || arrowStack.getType() != Material.ARROW || arrowStack.getAmount() <= 31) {

                        generatedArrows = new ItemStack(Material.ARROW);

                        if (arrowStack != null && (arrowStack.getType() == Material.ARROW
                                || arrowStack.getType() == Material.TIPPED_ARROW))
                            replaceCurrentArrowStack(arrowStack, generatedArrows, player);
                        else
                            // replaceCurrentStack -> currently not arrows
                            player.getInventory().setItem(17, generatedArrows);
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 140);
    }

    private void replaceCurrentArrowStack(ItemStack currentArrowStack, ItemStack generatedArrows, Player player) {
        int stackAmount = currentArrowStack.getAmount();
        int generatedAmount = generatedArrows.getAmount();
        generatedArrows.setAmount(Math.min((stackAmount + generatedAmount), 32));
        player.getInventory().setItem(17, generatedArrows);
    }
}
