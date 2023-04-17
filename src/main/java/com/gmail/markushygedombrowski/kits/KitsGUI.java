package com.gmail.markushygedombrowski.kits;

import com.gmail.markushygedombrowski.HLKits;
import com.gmail.markushygedombrowski.utils.Settings;
import com.gmail.markushygedombrowski.utils.KitsUtils;
import com.gmail.markushygedombrowski.utils.kitcooldown.Cooldown;
import com.gmail.markushygedombrowski.utils.kitcooldown.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitsGUI implements Listener {
    private final int RANDOM_INDEX = 9;
    private final int VIP_INDEX = 11;
    private final int TITAN_INDEX = 13;
    private final int MAFIA_INDEX = 15;
    private final int MAFIABOSS_INDEX = 17;
    private final int EXTRA_INDEX = 27;
    private final int IRON_INDEX = 29;
    private final int DEEPER_INDEX = 31;
    private final int CASINO_INDEX = 33;
    private final int LEGEND_INDEX = 35;
    private final int YT_INDEX = 21;
    private final int STREAMER_INDEX = 23;
    private Wool woolRed = new Wool(DyeColor.RED);
    private Wool woolGreen = new Wool(DyeColor.GREEN);
    private String region;
    private ItemStack random = woolRed.toItemStack(1);
    private ItemStack vip = woolRed.toItemStack(1);
    private ItemStack titan = woolRed.toItemStack(1);
    private ItemStack extra = woolRed.toItemStack(1);
    private ItemStack iron = woolRed.toItemStack(1);
    private ItemStack deeper = woolRed.toItemStack(1);
    private ItemStack casino = woolRed.toItemStack(1);
    private ItemStack legend = woolRed.toItemStack(1);
    private ItemStack mafia = woolRed.toItemStack(1);
    private ItemStack mafiaboss = woolRed.toItemStack(1);
    private ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 1);
    private KitsManager kitsManager;
    private HLKits plugin;
    private Settings settings;
    private String kitRegion;
    private Cooldown cooldown;
    private UtilTime utilTime;


    public KitsGUI(KitsManager kitsManager, HLKits plugin, Settings settings, Cooldown cooldown, UtilTime utilTime) {
        this.kitsManager = kitsManager;
        this.plugin = plugin;
        this.settings = settings;
        this.cooldown = cooldown;
        this.utilTime = utilTime;
    }

    public void create(Player p, String region) {
        Inventory inventory = Bukkit.createInventory(null, 45, region + "-Kits");
        this.region = region;
        if(region.equalsIgnoreCase("§aA")) {
            kitRegion = "A-";
        } else if (region.equalsIgnoreCase("§bB")) {
            kitRegion = "B-";
        } else {
            kitRegion = "C-";
        }
        Map<String, ItemStack> meta = new HashMap<>();
        List<String> lore = new ArrayList<>();
        meta.put("Random", random);
        meta.put("Vip", vip);
        meta.put("Titan", titan);
        meta.put("Extra", extra);
        meta.put("Iron", iron);
        meta.put("Deeper", deeper);
        meta.put("Casino", casino);
        meta.put("Legend", legend);
        meta.put("Mafia", mafia);
        meta.put("MafiaBoss", mafiaboss);
        meta.forEach((key, item) -> {
            ItemMeta itemMeta = item.getItemMeta();
            lore.clear();
            lore.add(0, "§7Du kan købe dette kit i §a§l/buy");
            if (p.hasPermission(key)) {

                if (!Cooldown.isCooling(p.getName(), kitRegion + key)) {
                    item = woolGreen.toItemStack(1);
                    lore.set(0, "§7Tag dit kit §a" + key);
                } else {
                    lore.set(0, "§7Du kan tage kit §a" + key + "§7 om §b" + (int) Cooldown.getRemaining(p.getName(), kitRegion + key) + "§7 " + UtilTime.getTimestamp());
                }

            }
            itemMeta.setDisplayName("§f§lKit " + key);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            meta.replace(key, item);
        });



        inventory.setItem(RANDOM_INDEX, meta.get("Random"));
        inventory.setItem(VIP_INDEX, meta.get("Vip"));
        inventory.setItem(TITAN_INDEX, meta.get("Titan"));
        inventory.setItem(MAFIA_INDEX, meta.get("Mafia"));
        inventory.setItem(MAFIABOSS_INDEX, meta.get("MafiaBoss"));
        inventory.setItem(EXTRA_INDEX, meta.get("Extra"));
        inventory.setItem(IRON_INDEX, meta.get("Iron"));
        inventory.setItem(DEEPER_INDEX, meta.get("Deeper"));
        inventory.setItem(CASINO_INDEX, meta.get("Casino"));
        inventory.setItem(LEGEND_INDEX, meta.get("Legend"));

        ItemMeta metaGlass = glass.getItemMeta();
        metaGlass.setDisplayName(" ");
        lore.set(0,"§7Mangler du rank på vores Discord");
        lore.add(1,"§7Skriv i Hjælp kanalen");
        metaGlass.setLore(lore);
        glass.setItemMeta(metaGlass);
        for(int x = 0; x < inventory.getSize(); x++) {
            if((inventory.getItem(x) == null)) {
                inventory.setItem(x,glass);
            }
        }


        p.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {


        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        int time = settings.getNormalkitTime();
        String kit = null;
        String name = null;

        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().contains("-Kits")) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            switch (clickedSlot) {
                case RANDOM_INDEX:
                    kit = kitRegion + "Random";
                    name = "Random";
                    break;
                case VIP_INDEX:
                    kit = kitRegion + "Vip";
                    name = "Vip";
                    break;
                case TITAN_INDEX:
                    kit = kitRegion + "Titan";
                    name = "Titan";
                    break;
                case MAFIA_INDEX:
                    kit = kitRegion + "Mafia";
                    name = "Mafia";
                    break;
                case MAFIABOSS_INDEX:
                    kit = kitRegion + "MafiaBoss";
                    name = "MafiaBoss";
                    break;
                case EXTRA_INDEX:
                    kit = kitRegion + "Extra";
                    name = "Extra";
                    break;
                case IRON_INDEX:
                    kit = kitRegion + "Iron";
                    name = "Iron";
                    break;
                case DEEPER_INDEX:
                    kit = kitRegion + "Deeper";
                    name = "Deeper";
                    break;
                case CASINO_INDEX:
                    kit = kitRegion + "Casino";
                    name = "Casino";
                    break;
                case LEGEND_INDEX:
                    kit = kitRegion + "Legend";
                    name = "Legend";
                    break;

            }

            if(kit == null) {

                return;
            }
            if(!p.hasPermission(name)) {
                p.sendMessage("§7Det har du §cikke adgang §7til!");
                p.sendMessage("§7Køb adgang til kit §a" + name + " §7via §a§l/buy");
                return;
            }
            KitsInfo kits = kitsManager.getKitsInfo(kit);
            if (kits == null) {
                p.sendMessage("§cThat kit doesn't exist");
                return;
            }
            if(Cooldown.isCooling(p.getName(),kit)) {
                int remaining = (int) Cooldown.getRemaining(p.getName(),kit);
                p.sendMessage("§8[§6§lKits§8]§7Du kan først tage det kit §a" + kit + "§7 om §b" + remaining + "§7 " + UtilTime.getTimestamp());
                return;
            } else {
                p.sendMessage("§8[§6§lKits§8]§7Du har taget kit §a " + kit);
                Cooldown.add(p.getName(),kit,time, System.currentTimeMillis());
                KitsUtils.addItems(p, kits.getItems());

                plugin.econ.depositPlayer(p, kits.getMoney());
                p.closeInventory();
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }
    }



}
