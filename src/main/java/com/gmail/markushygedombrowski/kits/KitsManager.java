package com.gmail.markushygedombrowski.kits;


import com.gmail.markushygedombrowski.utils.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitsManager {

    private ConfigManager configM;
    private Map<String , KitsInfo> kitsMap = new HashMap<>();
    public KitsManager(ConfigManager configM) {
        this.configM = configM;
    }



    public void load() {
        kitsMap.clear();
        FileConfiguration config = configM.getKits();
        for (String kit : config.getConfigurationSection("kits").getKeys(false)) {
            List<ItemStack> item = ((List<ItemStack>) config.get("kits." + kit + ".items"));
            int money = config.getInt("kits." + kit + ".money");
            KitsInfo kitsInfo = new KitsInfo(kit,item,money);
            kitsMap.put(kit,kitsInfo);
        }



    }
    public void save(KitsInfo kitsInfo) {
        FileConfiguration config = configM.getKits();
        config.set("kits." + kitsInfo.getName() + ".items", kitsInfo.getItems());
        config.set("kits." + kitsInfo.getName() + ".money", kitsInfo.getMoney());
        configM.saveKits();
        kitsMap.put(kitsInfo.getName(),kitsInfo);

    }
    public void delete(String name) {
        FileConfiguration config = configM.getKits();
        config.set("kits." + name,null);
        configM.saveKits();
        kitsMap.remove(name);
    }
    public void replace(String name, KitsInfo kitsInfo) {
        FileConfiguration config = configM.getKits();
        config.set("kits." + name,null);
        config.set("kits." + kitsInfo.getName() + ".items", kitsInfo.getItems());
        config.set("kits." + kitsInfo.getName() + ".money", kitsInfo.getMoney());
        configM.saveKits();
        kitsMap.remove(name);
        kitsMap.put(kitsInfo.getName(),kitsInfo);
    }
    public String getList() {
        String[] list = {""};
        kitsMap.forEach((k,v) -> {
            list[0] += k + ", ";
        });
        return list[0];
    }
    public KitsInfo getKitsInfo(String name) {
        return kitsMap.get(name);
    }
}
