package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Vanisher extends Kit {
    public static final String title = HexColor.ASSASSIN + KitClass.ASSASSIN + " Vanisher";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "Get " + ChatColor.WHITE + "Speed 2" + ChatColor.GRAY + " and become\n"
            + ChatColor.WHITE + "invisible" + ChatColor.GRAY + " and " + ChatColor.WHITE + "invulnerable"
            + ChatColor.GRAY + ".\n"
            + ChatColor.GRAY + "Your inventory will disappear\n"
            + ChatColor.GRAY + "for the abilities duration.\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 8 + ChatColor.GRAY + " seconds\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 40 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.ENDER_EYE;
    public static final int slot = 3;
}
