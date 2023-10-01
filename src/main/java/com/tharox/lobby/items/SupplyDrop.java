package com.tharox.lobby.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Random;

public class SupplyDrop {

    private ArrayList<Location> locations = new ArrayList<>();

    public SupplyDrop() {
        locations.add(new Location(Bukkit.getWorld("world"), 416, 166, 1300));
        locations.add(new Location(Bukkit.getWorld("world"), 544, 157, 1440));
        locations.add(new Location(Bukkit.getWorld("world"), 627, 155, 1303));
        locations.add(new Location(Bukkit.getWorld("world"), 512, 177, 1191));

        // Remove the previous beacon.
        for (Location location : locations) {
            location.getBlock().setType(Material.AIR);
        }

        // Place new beacon.
        Random rand = new Random();
        int number = rand.nextInt(locations.size());

        Location beaconLocation = locations.get(number);
        beaconLocation.getBlock().setType(Material.BEACON);

        // 33.3% chance to spawn Respawn Anchor, 66.7% chance to spawn Smiting Table
        Location lootLocation = new Location(beaconLocation.getWorld(), beaconLocation.getX() + 1,
                beaconLocation.getY() + 1, beaconLocation.getZ());

        number = rand.nextInt(3) + 1;

        if (number == 1) {
            lootLocation.getBlock().setType(Material.RESPAWN_ANCHOR);
        } else {
            lootLocation.getBlock().setType(Material.SMITHING_TABLE);
        }
    }
}
