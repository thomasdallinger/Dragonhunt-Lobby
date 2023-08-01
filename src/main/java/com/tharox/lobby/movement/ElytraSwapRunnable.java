package com.tharox.lobby.movement;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ElytraSwapRunnable extends BukkitRunnable {
    public static final HashMap<Player, ItemStack> playerChestplate = new HashMap<>();
    public static HashMap<Player, Boolean> playerWasInAir = new HashMap<>();

    public ElytraSwapRunnable() {
    }

    /**
     * Checks if the player is at least 2 or 6 blocks above the ground.
     *
     * @param player player to be checked if in the air
     * @return true if the player is at least 2 blocks above the ground and was in the air,<br>
     * true if the player is at least 8 blocks above the ground and was not in the air
     */
    private boolean isPlayerNowInTheAir(Player player) {
        if (playerWasInAir.get(player)) {
            for (int blocksBelow = 1; blocksBelow <= 2; blocksBelow++) {
                if (player.getLocation().subtract(0, blocksBelow, 0).getBlock().getType() != Material.AIR) {
                    return false;
                }
            }
        } else {
            for (int blocksBelow = 1; blocksBelow <= 8; blocksBelow++) {
                if (player.getLocation().subtract(0, blocksBelow, 0).getBlock().getType() != Material.AIR) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!playerWasInAir.containsKey(player)) continue;

            // player is no longer in the air - give the chestplate back (if it wasn't air)
            if (playerWasInAir.get(player) && !isPlayerNowInTheAir(player)) {
                playerWasInAir.put(player, false);
                player.getInventory().setChestplate(playerChestplate.get(player));
            }

            // player is currently in the air - replace the chestplate with an elytra
            else if (!playerWasInAir.get(player) && isPlayerNowInTheAir(player)) {

                // do nothing if the player is below y = 190
                if (player.getLocation().getY() < 190) continue;

                playerWasInAir.put(player, true);
                ItemStack chestplate = player.getInventory().getChestplate();

                if (chestplate != null) {
                    if (chestplate.getType() == Material.ELYTRA) {
                        playerChestplate.put(player, new ItemStack(Material.AIR));
                    } else {
                        playerChestplate.put(player, chestplate);
                    }
                } else {
                    playerChestplate.put(player, new ItemStack(Material.AIR));
                }

                // Give the player an Elytra.

                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta meta = elytra.getItemMeta();

                if (meta != null) {
                    meta.setUnbreakable(true);
                    meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                elytra.setItemMeta(meta);

                player.getInventory().setChestplate(elytra);
            }
        }
    }
}
