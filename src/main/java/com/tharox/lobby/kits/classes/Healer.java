package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Healer extends Kit {
    public static final String title = HexColor.SUPPORT + KitClass.SUPPORT + " Healer";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "Instantly heal yourself " + ChatColor.WHITE + 6 + " ❤"
            + ChatColor.GRAY + ".\n"
            + HexColor.RED + "» Alert on Activation\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + "Instantly\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 50 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.POTION;
    public static final int slot = 20;
}
