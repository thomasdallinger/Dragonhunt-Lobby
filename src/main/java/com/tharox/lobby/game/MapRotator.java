package com.tharox.lobby.game;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MapRotator {
    public static Calendar calendar;

    public static ArrayList<World> worlds = new ArrayList<>();
    public static int index = 0;
    public static World currentWorld;

    public MapRotator() {
        worlds.add(Bukkit.getWorld("world"));
        worlds.add(Bukkit.getWorld("world_nether"));
        worlds.add(Bukkit.getWorld("world_the_end"));
    }

    public static void changeMap() {
        calendar = new GregorianCalendar();
        int minute = calendar.get(Calendar.MINUTE);
        if (minute % 30 == 0) {
            index++;
            if (index > 2) {
                index = 0;
            }

            currentWorld = worlds.get(index);
        }
    }
}
