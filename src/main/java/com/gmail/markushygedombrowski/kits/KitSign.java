package com.gmail.markushygedombrowski.kits;

import com.gmail.markushygedombrowski.utils.KitsUtils;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitSign implements Listener {
    private  KitsGUI kitsGUI;

    public KitSign(KitsGUI kitsGUI) {
        this.kitsGUI = kitsGUI;
    }

    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if(sign.getLine(0).equalsIgnoreCase("§8======§2Kits§8======") &&
                        sign.getLine(1).equalsIgnoreCase("§8Klik her") &&
                        sign.getLine(2).equalsIgnoreCase("§8Køb adgang §a§l/buy") &&
                        sign.getLine(3).equalsIgnoreCase("§8===============") ) {
                    if(KitsUtils.isLocInRegion(p.getLocation(),"A")) {
                        kitsGUI.create(p,"§aA");
                    } else if (KitsUtils.isLocInRegion(p.getLocation(),"B")) {
                        kitsGUI.create(p,"§bB");
                    } else  {
                        kitsGUI.create(p,"§cC");
                    }
                }
            }
        }
    }
}
