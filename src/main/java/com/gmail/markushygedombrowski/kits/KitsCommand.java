package com.gmail.markushygedombrowski.kits;

import com.gmail.markushygedombrowski.utils.KitsUtils;
import com.gmail.markushygedombrowski.utils.kitcooldown.Cooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitsCommand implements CommandExecutor {
    private KitsManager kitsManager;

    public KitsCommand(KitsManager kitsManager) {

        this.kitsManager = kitsManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!(sender instanceof Player)) {
            System.out.println("§cKan kun bruges af Players");
            return true;
        }
        Player cmdPlayer = (Player) sender;
        if(!cmdPlayer.hasPermission("admin.kits")) {
            cmdPlayer.sendMessage("§cDet har du ikke permission til!");
            return true;
        }
        if(args.length == 0) {
            cmdPlayer.sendMessage("§8[§6§lKits§8]§7 /hlcreatekit <kit> <money>");
            cmdPlayer.sendMessage("§8[§6§lKits§8]§7 /hlgetkit <kit>  <optional player>");
            cmdPlayer.sendMessage("§8[§6§lKits§8]§7 /hlkittimereset <kit> <player>");
            cmdPlayer.sendMessage("§8[§6§lKits§8]§7 /hldeletekit <kit>");
            return true;
        }
        String kit = args[0];
        String playerName;
        if(args.length == 2) {
            playerName = args[1];
        } else {
            playerName = cmdPlayer.getName();
        }
        KitsInfo info = kitsManager.getKitsInfo(kit);
        if (alias.equalsIgnoreCase("hlcreatekit")) {
            if(args[1] == null) {
                cmdPlayer.sendMessage("§aHusk penge");
                return true;
            }
            String money = args[1];
            if(!KitsUtils.isNumeric(money,cmdPlayer)) return true;

            createKit(cmdPlayer,kit,Integer.parseInt(money),info);
            cmdPlayer.sendMessage("§8[§6§lKits§8]§a " + kit + "§7 has been §acreated");
            return true;
        }
        if(alias.equalsIgnoreCase("hlkittimereset")) {
            String name = args[1];
            resetCooldown(cmdPlayer,kit,name);
            return true;
        }
        if(alias.equalsIgnoreCase("hlsettime")) {
            playerName = args[1];
            String time = args[2];
            if(!KitsUtils.isNumeric(time,cmdPlayer)) return true;
            setTimer(playerName,kit,Integer.parseInt(time));
            return true;
        }
        if(alias.equalsIgnoreCase("hldeletekit")) {
            removeKit(kit);
            return true;
        }
        if(alias.equalsIgnoreCase("hlreplacekit")) {
            String money = args[1];
            if(!KitsUtils.isNumeric(money,cmdPlayer)) return true;
            replaceKit(kit,cmdPlayer,Integer.parseInt(money));
            return true;
        }

        if (giveKit(sender, cmdPlayer, playerName, info)) return true;
        return true;

    }

    private boolean giveKit(CommandSender sender, Player p,String player, KitsInfo info) {
        if (info == null) {
            sender.sendMessage("§8[§6§lKits§8] §cThat kit doesn't exist");
            return true;
        }
        Player target = p.getServer().getPlayer(player);
        KitsUtils.addItems(target, info.getItems());
        if(target == p) {
            p.sendMessage("§8[§6§lKits§8]§7 Du har fået §a" + info.getName());
            return true;
        }
        p.sendMessage("§8[§6§lKits§8]§7 Du har givet §a" + player + "§7 kit §a" + info.getName());
        return false;
    }

    public void createKit(Player p, String kit,int money, KitsInfo info) {
        if (info != null) {
            p.sendMessage("§8[§6§lKits§8] §cThat kit already exist!");
            return;
        }
        List<ItemStack> saveitems = getItemStacks(p);
        info = new KitsInfo(kit,saveitems,money);
        kitsManager.save(info);
    }



    public void removeKit(String kit) {
        kitsManager.delete(kit);
    }


    public void replaceKit(String kit,Player p,int money) {
        KitsInfo info = kitsManager.getKitsInfo(kit);
        if(info == null) {
            p.sendMessage("§cThat kit doesn't exist");
            return;
        }

        List<ItemStack> newItems = getItemStacks(p);
        info.setItems(newItems);
        if(money != 0) info.setMoney(money);
        kitsManager.replace(kit,info);
    }

    public void setTimer(String player, String kit,int time) {
        Cooldown.add(player,kit,time,System.currentTimeMillis());
    }


    public void resetCooldown(Player p,String kit,String name) {
        if(!Cooldown.isCooling(name,kit)) {
            p.sendMessage("Har ikke cooldown");
            return;

        }
        p.sendMessage("§8[§6§lKits§8]§7 §aCooldown §7er blevet fjernet fra §a" + name + "§7 for §a" + kit);

        Cooldown.removeCooldown(name,kit);
    }
    private List<ItemStack> getItemStacks(Player p) {
        List<ItemStack> saveitems = new ArrayList<>();
        ItemStack[] items = p.getInventory().getContents();
        for (ItemStack item : items) {
            if(!(item == null)) saveitems.add(item);
        }
        return saveitems;
    }


}
