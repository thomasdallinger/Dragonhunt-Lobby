package com.tharox.lobby.kits.classes;

import com.tharox.lobby.chat.HexColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

@DragonhuntKit
public class Demolitionist extends Kit {
    public static final String title = HexColor.ASSASSIN + KitClass.ASSASSIN + " Demolitionist";
    public static final String lore = ""
            + ChatColor.DARK_GRAY + "ᴀʙɪʟɪᴛʏ\n"
            + ChatColor.GRAY + "On activation " + ChatColor.WHITE + "4" + ChatColor.GRAY + " layers of "
            + ChatColor.WHITE + "TNT\n"
            + ChatColor.GRAY + "will spawn below you.\n"
            + ChatColor.GRAY + "During the explosion you are invulnerable\n"
            + ChatColor.GRAY + "to damage and you cannot move.\n"
            + HexColor.RED + "» Glow and Alert on Activation\n\n"
            + ChatColor.DARK_GRAY + "ᴅᴜʀᴀᴛɪᴏɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 25 + ChatColor.GRAY + " seconds\n\n"
            + ChatColor.DARK_GRAY + "ᴄᴏᴏʟᴅᴏᴡɴ\n"
            + ChatColor.GRAY + ChatColor.WHITE + 120 + ChatColor.GRAY + " seconds\n\n";
    public static final Material material = Material.TNT;
    public static final int slot = 4;
}