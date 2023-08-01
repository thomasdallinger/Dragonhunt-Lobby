package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Grappler extends Kit {
    public static final String title = HexColor.MOBILITY + KitClass.MOBILITY + " Grappler";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "Pull yourself to different locations\n"
            + ChatColor.GRAY + "with your Grappling Hook.\n"
            + ChatColor.GRAY + "Arrows shot cannot do damage.\n"
            + ChatColor.GRAY + "Be aware of fall damage.\n\n"
            + ChatColor.GRAY + "Max. Arrow Distance: "
            + ChatColor.WHITE + "20" + " Blocks\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + "Instantly\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 10 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.CROSSBOW;
    public static final int slot = 29;
}
