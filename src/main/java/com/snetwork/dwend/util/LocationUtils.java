package com.snetwork.dwend.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {
    public static String locationToString(Location location) {
        return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " " + location.getWorld().getName();
    }

    public static Location stringToLocation(String location) {
        String[] split = location.split(" ");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int z = Integer.parseInt(split[2]);
        String world = split[3];
        World world1 = Bukkit.getWorld(world);
        return new Location(world1, x, y, z);
    }
}