package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Mutant extends Kit {
    public static final String title = HexColor.TANK + KitClass.TANK + " Mutant";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "On activation you receive: \n"
            + ChatColor.WHITE + "+40%" + ChatColor.GRAY + " Attack Speed\n"
            + ChatColor.WHITE + "+80%" + ChatColor.GRAY + " Mining Speed\n"
            + ChatColor.WHITE + "+" + 1.0 + " ❤"
            + ChatColor.GRAY + " Attack Damage\n"
            + ChatColor.WHITE + "+4 Absorption ❤\n"
            + HexColor.RED + "» Glow and Alert on Activation\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 8 + ChatColor.GRAY + " seconds\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 50 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.DIAMOND_CHESTPLATE;
    public static final int slot = 12;
}
