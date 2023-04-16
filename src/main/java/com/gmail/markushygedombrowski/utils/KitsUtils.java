package com.gmail.markushygedombrowski.utils;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class KitsUtils {

    public static void addItems(Player p, List<ItemStack> items) {
        for (ItemStack item : items) {
            p.getInventory().addItem(item);
        }
    }

    public static boolean isLocInRegion(Location loc, String regionName) {
        if (regionName == null) {
            return true;
        }
        ApplicableRegionSet set = getWGSet(loc);
        if (set == null) {
            return false;
        }
        for (ProtectedRegion r : set) {
            if (r.getId().equalsIgnoreCase(regionName)) {
                return true;
            }
        }
        return false;
    }

    private static ApplicableRegionSet getWGSet(Location loc) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) {
            return null;
        }
        RegionManager rm = wg.getRegionManager(loc.getWorld());
        if (rm == null) {
            return null;
        }
        return rm.getApplicableRegions(com.sk89q.worldguard.bukkit.BukkitUtil.toVector(loc));
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }


    public static List<Player> getPlayers(String perm) {
        List<Player> playerList = new ArrayList<>();

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.hasPermission(perm)) {
                playerList.add(all.getPlayer());
            }
        }
        return playerList;
    }

    public static int hoursToSeconds(int hours) {
        return (hours * 60 * 60);
    }


    public static boolean procent(double pro) {
        Random r = new Random();
        double num = r.nextInt(100);
        return num < pro;

    }

    public static boolean isNumeric(String string, Player p) {
        int intValue;


        if (string == null || string.equals("")) {

            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            p.sendMessage("Input String cannot be parsed to Integer.");
        }
        return false;
    }


}
















