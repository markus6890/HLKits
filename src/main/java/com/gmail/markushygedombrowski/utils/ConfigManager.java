package com.gmail.markushygedombrowski.utils;

import com.gmail.markushygedombrowski.HLKits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private HLKits plugin = HLKits.getPlugin(HLKits.class);

    public FileConfiguration warpscfg;
    public File warpsFile;
    public FileConfiguration itemcfg;
    public File itemFile;
    public FileConfiguration kitscfg;
    public File kitsFile;

    public void setup() {
        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();

        }

        kitsFile = new File(plugin.getDataFolder(),"kits.yml");



        if(!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            }catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not create kits.yml File");
            }
        }



        kitscfg = YamlConfiguration.loadConfiguration(kitsFile);

    }



    public FileConfiguration getKits() {
        return kitscfg;
    }

    public void saveKits () {
        try {
            kitscfg.save(kitsFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not create kits.yml File");
        }
    }

    public void reloadKits() {
        kitscfg = YamlConfiguration.loadConfiguration(kitsFile);
    }
}
