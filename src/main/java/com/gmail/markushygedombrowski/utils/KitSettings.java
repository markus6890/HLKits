package com.gmail.markushygedombrowski.utils;

import org.bukkit.configuration.file.FileConfiguration;


public class KitSettings {


    private int normalkitTime;
    private String database;
    private String host;
    private int port;
    private String user;
    private String password;



    public void load(FileConfiguration config) {
        this.normalkitTime = config.getInt("normalkitTime");
        this.database = config.getString("sql.database");
        this.host = config.getString("sql.host");
        this.port = config.getInt("sql.port");
        this.user = config.getString("sql.user");
        this.password = config.getString("sql.password");
    }

    public int getNormalkitTime() {
        return (normalkitTime * 60 * 60);

    }
    public String getDatabase() {
        return database;
    }

    public String getHost() {
        System.out.println(host);
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
