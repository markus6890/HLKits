package com.gmail.markushygedombrowski.kits;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitsInfo {
    private String name;
    private List<ItemStack> items;
    private int money;
    private String timestamp;

    public KitsInfo(String name, List<ItemStack> items, int money) {
        this.name = name;
        this.items = items;
        this.money = money;
    }

    public String getName() {
        return name;
    }
    public List<ItemStack> getItems() {
        return items;
    }
    public int getMoney() {
        return money;
    }
}
