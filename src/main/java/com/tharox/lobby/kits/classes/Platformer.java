package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Platformer extends Kit {
    public static final String title = HexColor.MOBILITY + KitClass.MOBILITY + " Platformer";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "Every second blocks will spawn below you\n"
            + ChatColor.GRAY + "making it possible to walk everywhere. \n"
            + ChatColor.GRAY + "You can go up, down and through (most) blocks. \n"
            + ChatColor.GRAY + "You receive " + ChatColor.WHITE + "Speed 3" + ChatColor.GRAY + ".\n"
            + HexColor.RED + "» Glow and Alert on Activation\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 16 + ChatColor.GRAY + " seconds\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 70 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.PINK_STAINED_GLASS;
    public static final int slot = 30;
}