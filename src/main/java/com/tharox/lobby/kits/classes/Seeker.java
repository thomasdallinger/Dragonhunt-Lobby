package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Seeker extends Kit {
    public static final String title = HexColor.MOBILITY + KitClass.MOBILITY + " Seeker";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "On activation you execute the " + ChatColor.WHITE + "/scout"
            + ChatColor.GRAY + " command.\n\n"
            + ChatColor.GRAY + "While in a Scout you can teleport to the\n"
            + ChatColor.GRAY + "location you fly to using " + ChatColor.WHITE + "/scout tp" + ChatColor.GRAY + ".\n"
            + ChatColor.GRAY + "After the TP you instantly have " + ChatColor.WHITE + "Weakness 3\n"
            + ChatColor.GRAY + "and " + ChatColor.WHITE + "Slowness 2" + ChatColor.GRAY + " for "
            + ChatColor.WHITE + "10" + ChatColor.GRAY + " seconds.\n"
            + ChatColor.GRAY + "The TP only works in the " + ChatColor.WHITE + "same dimension" + ChatColor.GRAY + ".\n"
            + HexColor.RED + "» Alert on Activation\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + "Instantly\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 0 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.ENDER_PEARL;
    public static final int slot = 31;
}
