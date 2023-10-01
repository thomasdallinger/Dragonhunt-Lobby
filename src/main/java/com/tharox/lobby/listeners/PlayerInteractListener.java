package com.tharox.lobby.listeners;

import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.chat.Prefixes;
import com.tharox.lobby.game.GameState;
import com.tharox.lobby.kits.selection.KitInventory;
import com.tharox.lobby.queue.Queue;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PlayerInteractListener implements Listener {
    public static HashMap<Player, Long> queueCooldown = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack item = player.getInventory().getItemInMainHand();
        Date date = new Date();

        EquipmentSlot equipmentSlot = event.getHand();
        if (equipmentSlot == null)
            return;

        if (item.getItemMeta() == null) { // no item in hand
            cannotInteract(event, player, block, equipmentSlot);
            return;
        }

        String itemName = item.getItemMeta().getDisplayName();

        if (equipmentSlot.equals(EquipmentSlot.HAND)
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            // game queue in hand
            if (itemName.contains(HexColor.YELLOW + "Play")) {
                // not able to equip / place the item
                event.setCancelled(true);

                // Player is already in a Game Queue
                if (Queue.inQueue.contains(player)) {
                    leaveQueue(player);
                    return;
                }

                if (Queue.serverID == 0) {
                    player.sendMessage(Prefixes.getErrorPrefix() + "All " + org.bukkit.ChatColor.DARK_RED + "Servers "
                            + org.bukkit.ChatColor.RED + "are currently full. Please try again later.");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                    return;
                }

                if (Queue.inQueue.size() == 8) {
                    player.sendMessage(Prefixes.getErrorPrefix() + "The " + org.bukkit.ChatColor.DARK_RED + "Game Queue "
                            + ChatColor.RED + "is currently full. Please try again later.");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                    return;
                }

                // player has already been in the Queue at least once and has left it
                if (queueCooldown.containsKey(player)) {
                    // check if the player is still on cooldown
                    if (queueCooldown.get(player) > date.getTime()) {
                        player.sendMessage(Prefixes.getErrorPrefix()
                                + "Please wait a few seconds before joining the queue again.");
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                        return;
                    }
                }

                joinQueue(player);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                return;

            } else if (itemName.contains(HexColor.YELLOW + "Purchase Kits")) {
                event.setCancelled(true);
                player.openInventory(new KitInventory(player).kits);
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
                return;

            } else if (itemName.contains(HexColor.YELLOW + "Profile")) {
                event.setCancelled(true);

                player.sendMessage(Prefixes.getErrorPrefix() + "Managing your profile will be available soon.");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);

                //player.openInventory(new ProfileInventory(player).menu);
                //player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
                return;
            }
        }

        // Modifiy the functionality of the Fletching Table and Magma Block
        if (equipmentSlot.equals(EquipmentSlot.HAND)
                && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();

            if (GameState.protectedItemMaterials.contains(mainHandItem.getType()) || event.getClickedBlock() == null) {
                return;
            }

            if (event.getClickedBlock().getType() == Material.FLETCHING_TABLE) {
                if (!mainHandItem.getEnchantments().isEmpty()) {
                    removeEnchantments(mainHandItem);
                    player.sendMessage(HexColor.GREEN + "Item in Main Hand disenchanted.");
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                    player.setLevel(player.getLevel() + GameState.levelsOnDisenchanting);
                }

                // No more interaction possible such as equipping the disenchanted item (armor).
                return;
            }

            if (event.getClickedBlock().getType() == Material.MAGMA_BLOCK) {
                player.getInventory().remove(player.getInventory().getItemInMainHand());
                player.sendMessage(HexColor.GREEN + "Item in Main Hand deleted.");
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);

                return;
            }
        }

        // item in hand, that is not null, the Game Queue or Purchase Kits
        // example: chicken can drop eggs
        cannotInteract(event, player, block, equipmentSlot);
    }

    private void cannotInteract(PlayerInteractEvent event, Player player, Block block, EquipmentSlot e) {
        ArrayList<Material> allowedItems = new ArrayList<>();
        allowedItems.add(Material.ENCHANTING_TABLE);
        allowedItems.add(Material.ANVIL);
        allowedItems.add(Material.ENDER_CHEST);
        allowedItems.add(Material.CHEST);
        allowedItems.add(Material.BARREL);
        allowedItems.add(Material.SMITHING_TABLE);
        allowedItems.add(Material.RESPAWN_ANCHOR);
        allowedItems.add(Material.BREWING_STAND);

        if (e.equals(EquipmentSlot.HAND)
                && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // Players can interact with enchanting tables, anvils and loot blocks.
            if (!allowedItems.contains(block.getType())) {
                if (!player.isOp()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void joinQueue(Player player) {
        Queue.inQueue.add(player);
        player.sendMessage(HexColor.GREEN + "You joined the queue.");
        player.sendMessage(ChatColor.GRAY + "You can leave using "
                + ChatColor.WHITE + "Right-Click" + ChatColor.GRAY + " again.");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    private void leaveQueue(Player player) {
        Queue.inQueue.remove(player);
        player.sendMessage(HexColor.RED + "You left the queue.");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

        Date date = new Date();
        queueCooldown.put(player, date.getTime() + 7000); // Cooldown: 7 seconds = 7000 milliseconds
    }

    private void removeEnchantments(ItemStack itemStack) {
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            itemStack.setType(Material.BOOK);
        }

        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            itemStack.removeEnchantment(enchantment);
        }
    }
}