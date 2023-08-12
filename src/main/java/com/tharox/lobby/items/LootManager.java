package com.tharox.lobby.items;

import com.tharox.lobby.Main;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.game.GameState;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootManager implements Listener {

    private final String[] rarities = {"uncommon", "rare", "epic", "exotic", "mythic"};
    public static final List<LootItem> uncommon = new ArrayList<>();
    public static final List<LootItem> rare = new ArrayList<>();
    public static final List<LootItem> epic = new ArrayList<>();
    public static final List<LootItem> exotic = new ArrayList<>();
    public static final List<LootItem> mythic = new ArrayList<>();
    public static final List<PotionEffectType> positiveEffects = new ArrayList<>();
    public static final List<PotionEffectType> randomEffects = new ArrayList<>();

    public LootManager(FileConfiguration lootConfig) {
        for (String rarity : rarities) {
            ConfigurationSection itemsSection = lootConfig.getConfigurationSection(rarity);

            if (itemsSection == null) {
                Bukkit.getLogger().severe("Setup config.yml");
                return;
            }

            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection section = itemsSection.getConfigurationSection(key);
                switch (rarity) {
                    case "uncommon":
                        uncommon.add(new LootItem(section));
                        break;
                    case "rare":
                        rare.add(new LootItem(section));
                        break;
                    case "epic":
                        epic.add(new LootItem(section));
                        break;
                    case "exotic":
                        exotic.add(new LootItem(section));
                        break;
                    case "mythic":
                        mythic.add(new LootItem(section));
                        break;
                }

            }
        }

        positiveEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
        positiveEffects.add(PotionEffectType.ABSORPTION);
        positiveEffects.add(PotionEffectType.SPEED);
        positiveEffects.add(PotionEffectType.JUMP);
        positiveEffects.add(PotionEffectType.REGENERATION);

        randomEffects.addAll(positiveEffects);
        randomEffects.add(PotionEffectType.POISON);
        randomEffects.add(PotionEffectType.SLOW);
        randomEffects.add(PotionEffectType.WEAKNESS);
        randomEffects.add(PotionEffectType.BLINDNESS);
        randomEffects.add(PotionEffectType.LEVITATION);
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null || (block.getType() != Material.CHEST
                && block.getType() != Material.BARREL
                && block.getType() != Material.SMITHING_TABLE
                && block.getType() != Material.RESPAWN_ANCHOR)) {
            return;
        }

        World world = block.getWorld();
        Location location = block.getLocation();
        LootItem randomItem;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            // Chest: 1x Uncommon, 1x Rare
            if (block.getType() == Material.CHEST) {
                randomItem = uncommon.get(random.nextInt(uncommon.size()));
                world.dropItemNaturally(location, randomItem.make(random));

                randomItem = rare.get(random.nextInt(rare.size()));
                world.dropItemNaturally(location, randomItem.make(random));
            }

            // Barrel: 1x Rare, 1x Epic
            if (block.getType() == Material.BARREL) {
                randomItem = rare.get(random.nextInt(rare.size()));
                world.dropItemNaturally(location, randomItem.make(random));

                randomItem = epic.get(random.nextInt(epic.size()));
                world.dropItemNaturally(location, randomItem.make(random));
            }

            // Smithing Table: 1x Epic, 1x Exotic
            if (block.getType() == Material.SMITHING_TABLE) {
                randomItem = epic.get(random.nextInt(epic.size()));
                world.dropItemNaturally(location, randomItem.make(random));

                randomItem = exotic.get(random.nextInt(exotic.size()));
                world.dropItemNaturally(location, randomItem.make(random));
            }

            // Respawn Anchor: 1x Mythic or Grappler
            if (block.getType() == Material.RESPAWN_ANCHOR) {
                Random rand = new Random();
                int chance = rand.nextInt(10) + 1;

                if (chance <= 9) {
                    randomItem = mythic.get(random.nextInt(mythic.size()));
                    world.dropItemNaturally(location, randomItem.make(random));
                } else {
                    world.dropItemNaturally(location, Objects.requireNonNull(GameState.createGrappler()));
                }
            }

            // POSITIVE EFFECTS: Carved Pumpkin
            if (block.getType() == Material.CARVED_PUMPKIN) {
                Random rand = new Random();
                int chance = rand.nextInt(5) + 1;

                player.addPotionEffect(new PotionEffect(positiveEffects.get(chance), 200, 0));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            }

            // RANDOM EFFECTS: Jack o Lantern
            if (block.getType() == Material.JACK_O_LANTERN) {
                Random rand = new Random();
                int chance = rand.nextInt(5) + 1;

                player.addPotionEffect(new PotionEffect(randomEffects.get(chance), 300, 1));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            }

            block.setType(Material.BEDROCK);

            player.setLevel(player.getLevel() + GameState.levelsOnLooting);
            player.sendTitle("", HexColor.GREEN + "+" + GameState.levelsOnLooting
                            + ChatColor.WHITE + " Level", 0, 30, 10);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            // Reset the loot 2 minutes after the click.
            // 50% chance chest, 25% chance barrel, 15% chance smithing table, 5% chance respawn anchor
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {
                Random rand = new Random();
                int chance = rand.nextInt(20) + 1;

                if (chance <= 10) {
                    block.setType(Material.CHEST);
                } else if (chance <= 15) {
                    block.setType(Material.BARREL);
                } else if (chance <= 19) {
                    block.setType(Material.SMITHING_TABLE);
                } else {
                    block.setType(Material.RESPAWN_ANCHOR);
                }
            }, 2400L);
        }
    }
}