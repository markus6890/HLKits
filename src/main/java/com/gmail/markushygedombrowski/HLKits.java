package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.kits.KitSign;
import com.gmail.markushygedombrowski.kits.KitsCommand;
import com.gmail.markushygedombrowski.kits.KitsGUI;
import com.gmail.markushygedombrowski.kits.KitsManager;
import com.gmail.markushygedombrowski.utils.ConfigManager;
import com.gmail.markushygedombrowski.utils.ConfigReloadCmd;
import com.gmail.markushygedombrowski.utils.KitSettings;
import com.gmail.markushygedombrowski.utils.KitsUtils;
import com.gmail.markushygedombrowski.utils.kitcooldown.Cooldown;
import com.gmail.markushygedombrowski.utils.perms.SetPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class HLKits extends JavaPlugin {


    private KitSettings settings;
    private KitsManager kitsManager;
    private ConfigManager configM;

    public Economy econ = null;


        public void onEnable() {
            loadConfigManager();
            FileConfiguration config = getConfig();
            settings = new KitSettings();
            KitsUtils utils = new KitsUtils();
            settings.load(config);
            initKits();


            SetPerms setPerms = new SetPerms();
            Bukkit.getPluginManager().registerEvents(setPerms, this);

            try {
                loadCooldowns();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ConfigReloadCmd configReloadCmd = new ConfigReloadCmd(this);
            getCommand("hlkitreload").setExecutor(configReloadCmd);
            if (!setupEconomy()) {
                getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {
                    Cooldown.handleCooldowns();
                }
            }, 1L, 1L);

        }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public void reload() {

        reloadConfig();
        FileConfiguration config = getConfig();
        configM.reloadKits();
        kitsManager.load();
        settings.load(config);

    }
    public void loadConfigManager() {
        configM = new ConfigManager();
        configM.setup();
        configM.saveKits();
        configM.reloadKits();

    }

    public void initKits() {
        kitsManager = new KitsManager(configM);
        kitsManager.load();
        KitsCommand kitsCreateCommand = new KitsCommand(kitsManager);
        getCommand("hlgetkit").setExecutor(kitsCreateCommand);

        KitsGUI kitsGUI = new KitsGUI(kitsManager, this, settings);
        Bukkit.getPluginManager().registerEvents(kitsGUI, this);
        KitSign kitSign = new KitSign(kitsGUI);
        Bukkit.getPluginManager().registerEvents(kitSign, this);
    }

        public void onDisable() {
            saveCooldownSQL();
            System.out.println("==================================");
            System.out.println("HLKits disabled");
            System.out.println("==================================");
        }


    private void loadCooldowns() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM cooldown");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int time = resultSet.getInt("time");
            String kit = resultSet.getString("kit");
            time = time / 1000;
            if(!(time < 0)) {
                Cooldown.add(name, kit, time, System.currentTimeMillis());
                System.out.println("Loaded cooldown for " + name + " for kit " + kit + " for " + time + " seconds");
            }
            deleteFromSQL(connection, name);
        }
        closeAllSQL(connection, statement, resultSet);
    }

    private void deleteFromSQL(Connection connection, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM cooldown WHERE name = ?");
        statement.setString(1, name);
        statement.executeUpdate();
        statement.close();
    }


    private void saveCooldownSQL() {
        if (Cooldown.cooldownPlayers.isEmpty()) return;
        Connection connection = null;
        PreparedStatement[] statement = new PreparedStatement[1];
        try {
            connection = getConnection();
            statement[0] = connection.prepareStatement("INSERT INTO cooldown (name, time, kit) VALUES (?, ?, ?)");

            loopCooldowmMap(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAllSQL(connection, statement[0], null);
        }


    }


    private void loopCooldowmMap(PreparedStatement[] statement) {
        if (Cooldown.cooldownPlayers.isEmpty()) return;
        Cooldown.cooldownPlayers.forEach((key, value) -> {
            Player player = Bukkit.getPlayer(key);
            try {
                for (String name : Cooldown.cooldownPlayers.get(key).cooldownMap.keySet()) {
                    long time = (Cooldown.cooldownPlayers.get(key).cooldownMap.get(name).seconds + Cooldown.cooldownPlayers.get(key).cooldownMap.get(name).systime) - System.currentTimeMillis();
                    statement[0].setString(1, value.player);
                    statement[0].setLong(2, time);
                    statement[0].setString(3, name);
                    statement[0].execute();
                    System.out.println("Saved cooldown for " + value.player + " for kit " + name + " for " + time + " seconds");
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void closeAllSQL(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        String host = settings.getHost();
        int port = settings.getPort();
        String database = settings.getDatabase();
        String userName = settings.getUser();
        String password = settings.getPassword();

        String connectionString = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=utf8", host, port, database);


        return DriverManager.getConnection(connectionString, userName, password);
    }
}
