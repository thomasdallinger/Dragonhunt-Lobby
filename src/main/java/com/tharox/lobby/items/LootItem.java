package com.tharox.lobby.items;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {

    private final Material material;
    private final String customName;
    //private final int damage;

    private final Map<Enchantment, Integer> enchantmentToLevelMap = new HashMap<>();

    private final Map<PotionEffectType, Integer> effectToAmplifier = new HashMap<>();
    private int duration = 1200;
    private Color color = Color.RED;

    private final int minAmount;
    private final int maxAmount;

    public LootItem(ConfigurationSection section) {
        Material material;

        try {
            material = Material.valueOf(section.getString("material"));
        } catch (Exception e) {
            material = Material.AIR;
        }

        this.material = material;
        this.customName = section.getString("name");
        //this.damage = section.getInt("damage");

        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");

        if (enchantmentsSection != null) {
            for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(
                        NamespacedKey.minecraft(
                                enchantmentKey.toLowerCase()
                        )
                );

                if (enchantment != null) {
                    int level = enchantmentsSection.getInt(enchantmentKey);
                    enchantmentToLevelMap.put(enchantment, level);
                }
            }
        }

        ConfigurationSection effectsSection = section.getConfigurationSection("effects");

        if (effectsSection != null) {
            for (String effectKey : effectsSection.getKeys(false)) {
                PotionEffectType effect = PotionEffectType.getByName(effectKey.toLowerCase());

                if (effect != null) {
                    int amplifier = effectsSection.getInt(effectKey);
                    effectToAmplifier.put(effect, amplifier);
                }
            }
            this.duration = section.getInt("duration");
            this.color = Color.fromRGB(section.getInt("color"));

        }

        this.minAmount = section.getInt("minAmount");
        this.maxAmount = section.getInt("maxAmount");
    }

    public ItemStack make(ThreadLocalRandom random) {
        int amount = random.nextInt(minAmount, maxAmount + 1);
        ItemStack itemStack = new ItemStack(material, amount);

        if (!enchantmentToLevelMap.isEmpty()) {

            ItemMeta itemMeta = itemStack.getItemMeta();

            for (Map.Entry<Enchantment, Integer> enchantEntry : enchantmentToLevelMap.entrySet()) {
                itemMeta.addEnchant(
                        enchantEntry.getKey(),
                        enchantEntry.getValue(),
                        true
                );
            }

            if (customName != null) {
                itemMeta.setDisplayName(
                        ChatColor.translateAlternateColorCodes('&', customName)
                );
            }

            /*if (damage != null && itemMeta instanceof Damageable) {
                ((Damageable) itemMeta).setDamage(damage);
            }*/

            itemStack.setItemMeta(itemMeta);
        }

        if (!effectToAmplifier.isEmpty()) {

            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

            for (Map.Entry<PotionEffectType, Integer> effectEntry : effectToAmplifier.entrySet()) {
                potionMeta.addCustomEffect(new PotionEffect(
                                effectEntry.getKey(),
                                duration,
                                effectEntry.getValue()),
                        true
                );
            }

            potionMeta.setColor(color);

            if (customName != null) {
                potionMeta.setDisplayName(
                        ChatColor.translateAlternateColorCodes('&', customName)
                );
            }

            itemStack.setItemMeta(potionMeta);
        }

        return itemStack;
    }

    /**
     * Used for Loot Table display.
     */
    public ItemStack make() {
        ItemStack itemStack = new ItemStack(material, maxAmount);

        if (!enchantmentToLevelMap.isEmpty()) {

            ItemMeta itemMeta = itemStack.getItemMeta();

            for (Map.Entry<Enchantment, Integer> enchantEntry : enchantmentToLevelMap.entrySet()) {
                itemMeta.addEnchant(
                        enchantEntry.getKey(),
                        enchantEntry.getValue(),
                        true
                );
            }

            if (customName != null) {
                itemMeta.setDisplayName(
                        ChatColor.translateAlternateColorCodes('&', customName)
                );
            }

            /*if (damage != null && itemMeta instanceof Damageable) {
                ((Damageable) itemMeta).setDamage(damage);
            }*/

            itemStack.setItemMeta(itemMeta);
        }

        if (!effectToAmplifier.isEmpty()) {

            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

            for (Map.Entry<PotionEffectType, Integer> effectEntry : effectToAmplifier.entrySet()) {
                potionMeta.addCustomEffect(new PotionEffect(
                                effectEntry.getKey(),
                                duration,
                                effectEntry.getValue()),
                        true
                );
            }

            potionMeta.setColor(color);

            if (customName != null) {
                potionMeta.setDisplayName(
                        ChatColor.translateAlternateColorCodes('&', customName)
                );
            }

            itemStack.setItemMeta(potionMeta);
        }

        return itemStack;
    }
}
