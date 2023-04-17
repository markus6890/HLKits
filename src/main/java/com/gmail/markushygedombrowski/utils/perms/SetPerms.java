package com.gmail.markushygedombrowski.utils.perms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetPerms implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(p.hasPermission("essentials.kits.Casino")) {
           Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset essentials.kits.Casino true");
           Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set casino true prison");
        }
        if(p.hasPermission("essentials.kits.Deeber")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset essentials.kits.Deeber true");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set deeper true prison");
        }
        if(p.hasPermission("essentials.kits.ekstra")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset essentials.kits.ekstra true");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set extra true prison");
        }
        if(p.hasPermission("essentials.kits.iron")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset essentials.kits.iron true");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set iron true prison");
        }

    }
}
