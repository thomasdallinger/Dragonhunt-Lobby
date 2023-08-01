package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Wizard extends Kit {
    public static final String title = HexColor.SUPPORT + KitClass.SUPPORT + " Wizard";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "Opens the " + ChatColor.WHITE + "Enchanting" + ChatColor.GRAY + " inventory.\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + "Instantly\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 0 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.ENCHANTING_TABLE;
    public static final int slot = 21;
}
