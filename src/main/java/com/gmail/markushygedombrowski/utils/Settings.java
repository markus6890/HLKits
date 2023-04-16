package com.gmail.markushygedombrowski.utils;

import org.bukkit.configuration.file.FileConfiguration;


public class Settings {


    private int normalkitTime;

    public void load(FileConfiguration config) {
        this.normalkitTime = config.getInt("normalkitTime");
    }

    public int getNormalkitTime() {
        return (normalkitTime * 60 * 60);

    }
}
