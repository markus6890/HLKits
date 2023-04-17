package com.gmail.markushygedombrowski.kits;

import com.gmail.markushygedombrowski.HLKits;
import com.gmail.markushygedombrowski.utils.ConfigManager;
import com.gmail.markushygedombrowski.utils.KitsUtils;
import com.gmail.markushygedombrowski.utils.kitcooldown.Cooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitsCreateCommand implements CommandExecutor {
    private HLKits plugin;
    private ConfigManager configM;
    private Cooldown cooldown;
    private KitsManager kitsManager;

    public KitsCreateCommand(HLKits plugin, ConfigManager configM, Cooldown cooldown, KitsManager kitsManager) {
        this.plugin = plugin;
        this.configM = configM;
        this.cooldown = cooldown;
        this.kitsManager = kitsManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!(sender instanceof Player)) {
            System.out.println("§cKan kun bruges af Players");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("admin")) {
            p.sendMessage("§cDet har du ikke permission til!");
            return true;
        }
        if(args.length == 0) {
            p.sendMessage("§8[§6§lKits§8]§7 /hlcreatekit <kit> <money>");
            p.sendMessage("§8[§6§lKits§8]§7 /hlgetkit <kit>");
            p.sendMessage("§8[§6§lKits§8]§7 /hlkittimereset <kit> <player>");
            return true;
        }
        String kit = args[0];
        KitsInfo info = kitsManager.getKitsInfo(kit);
        if (alias.equalsIgnoreCase("hlcreatekit")) {
            if(args[1] == null) {
                p.sendMessage("§aHusk penge");
                return true;
            }
            String money = args[1];
            if(!KitsUtils.isNumeric(money,p)) return true;

            createKit(p,kit,Integer.parseInt(money),info);
            p.sendMessage("§8[§6§lKits§8]§a " + kit + "§7 has benn §acreated");
            return true;
        }
        if(alias.equalsIgnoreCase("hlkittimereset")) {
            String name = args[1];
            resetCooldown(p,kit,name);
            return true;
        }
        if (info == null) {
            sender.sendMessage("§8[§6§lKits§8] §cThat kit doesn't exist");
            return true;
        }

        KitsUtils.addItems(p,info.getItems());
        return true;

    }
    public void createKit(Player p, String kit,int money, KitsInfo info) {
        if (info != null) {
            p.sendMessage("§8[§6§lKits§8] §cThat kit already exist!");
            return;
        }
        List<ItemStack> saveitems = new ArrayList<>();
        ItemStack[] items = p.getInventory().getContents();
        for (ItemStack item : items) {
            if(!(item == null)) saveitems.add(item);
        }
        info = new KitsInfo(kit,saveitems,money);
        kitsManager.save(info);
    }


    public void resetCooldown(Player p,String kit,String name) {
        if(!cooldown.isCooling(name,kit)) {
            p.sendMessage("Har ikke cooldown");
            return;

        }
        p.sendMessage("§8[§6§lKits§8]§7 §aCooldown §7er blevet fjernet fra §a" + name + "§7 for §a" + kit);

        cooldown.removeCooldown(name,kit);
    }


}
