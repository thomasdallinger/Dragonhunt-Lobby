package com.tharox.lobby;

import com.tharox.lobby.arrows.Arrows;
import com.tharox.lobby.blocks.BlockBreakListener;
import com.tharox.lobby.blocks.BlockPlaceListener;
import com.tharox.lobby.chat.ChatListener;
import com.tharox.lobby.chat.HexColor;
import com.tharox.lobby.commands.*;
import com.tharox.lobby.entities.EntityDamageByEntityListener;
import com.tharox.lobby.entities.EntityDamageListener;
import com.tharox.lobby.items.InventoryCloseListener;
import com.tharox.lobby.items.InventoryOpenListener;
import com.tharox.lobby.items.LootManager;
import com.tharox.lobby.items.LootTableListener;
import com.tharox.lobby.kits.selection.KitListener;
import com.tharox.lobby.listeners.*;
import com.tharox.lobby.movement.ElytraSwapRunnable;
import com.tharox.lobby.profiles.ProfileListener;
import com.tharox.lobby.queue.Queue;
import com.tharox.lobby.tablist.TablistManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public final class Main extends JavaPlugin implements PluginMessageListener {

    private static Main instance;
    private TablistManager tablistManager;

    public Main() {
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig(); // config.yml

        Bukkit.getLogger().fine("Plugin is activated");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        listenerRegistration();
        commandRegistration();

        setWorldBorders();
        tablistManager = new TablistManager();

        Queue.bossBar.setVisible(true);

        new ElytraSwapRunnable().runTaskTimer(Main.getInstance(), 0, 5);
        new Arrows();

        new BukkitRunnable() {
            @Override
            public void run() {
                Queue.onInterval();
                displayActionBar();
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().fine("Plugin is deactivated");
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    public static Main getInstance() {
        return instance;
    }

    public TablistManager getTablistManager() {
        return tablistManager;
    }

    public static String getTitle() {
        return HexColor.PURPLE.toString() + ChatColor.BOLD + "DRAGONHUNT";
    }

    private void displayActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (CombatListener.combatList.containsKey(player.getUniqueId())) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder("In Combat for ").color(ChatColor.GRAY)
                                .append(String.valueOf(CombatListener.combatList.get(player.getUniqueId())))
                                .color(HexColor.RED).bold(true)
                                .append(" seconds").color(ChatColor.GRAY).bold(false).create());
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder("Use ").color(ChatColor.GRAY)
                                .append("/spawn")
                                .color(HexColor.YELLOW).bold(true)
                                .append(" to teleport back to Spawn").color(ChatColor.GRAY).bold(false).create());
            }
        }
    }

    public static Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("world"), 521.5, 250, 1339.5, (float) -0.3, (float) -1.7);
    }

    /**
     * Sets a World Border in all playable dimensions.
     */
    private void setWorldBorders() {
        World overworld = Bukkit.getServer().getWorld("world");
        if (overworld != null) {
            WorldBorder worldBorder = overworld.getWorldBorder();
            worldBorder.setCenter(getSpawnLocation());
            worldBorder.setSize(500);
            worldBorder.setDamageAmount(0);
        }

        World nether = Bukkit.getServer().getWorld("world_nether");
        if (nether != null) {
            WorldBorder worldBorder = nether.getWorldBorder();
            worldBorder.setCenter(new Location(nether, 0, 0, 0));
            worldBorder.setSize(2000);
            worldBorder.setDamageAmount(0);
        }
    }

    public final static PluginManager pluginManager = Bukkit.getPluginManager();

    private void listenerRegistration() {
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new ItemConsumeListener(), this);
        pluginManager.registerEvents(new CombatListener(), this);

        pluginManager.registerEvents(new BlockBreakListener(), this);
        pluginManager.registerEvents(new BlockPlaceListener(), this);

        pluginManager.registerEvents(new LootManager(getConfig()), this);
        pluginManager.registerEvents(new LootTableListener(), this);
        pluginManager.registerEvents(new InventoryOpenListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);

        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);
        pluginManager.registerEvents(new EntityDamageListener(), this);

        pluginManager.registerEvents(new KitListener(), this);
        pluginManager.registerEvents(new ProfileListener(), this);

        pluginManager.registerEvents(new GrappleListener(), this);
    }

    private void commandRegistration() {
        Objects.requireNonNull(getCommand("date")).setExecutor(new DateCommand());
        Objects.requireNonNull(getCommand("forcestart")).setExecutor(new ForceStart());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("website")).setExecutor(new WebsiteCommand());
        Objects.requireNonNull(getCommand("youtube")).setExecutor(new YoutubeCommand());
        Objects.requireNonNull(getCommand("twitter")).setExecutor(new TwitterCommand());
        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("beacon")).setExecutor(new BeaconCommand());
        Objects.requireNonNull(getCommand("tier")).setExecutor(new LootCommand());
    }

    public static void setServerStatus(String action, int serverID) {
        if (!(action.equals("start") || action.equals("stop"))) {
            return;
        }

        try {
            URL url = new URL("http://192.168.1.219:5000/" + action + "/" + serverID + "?accessToken=REMOVED_TOKEN");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.connect();
            connection.getResponseCode();
            connection.disconnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    }
}
