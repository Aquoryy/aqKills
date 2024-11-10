package org.aqu0ryy.kills;

import org.aqu0ryy.kills.api.PlayerPointsApi;
import org.aqu0ryy.kills.api.TokenManagerApi;
import org.aqu0ryy.kills.api.VaultApi;
import org.aqu0ryy.kills.utils.ChatUtil;
import org.aqu0ryy.kills.listeners.KillListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loader extends JavaPlugin {

    private static Loader inst;

    @Override
    public void onEnable() {
        inst = this;

        if (VaultApi.setupVault() || PlayerPointsApi.setupPlayerPoints() || TokenManagerApi.setupTokenManager()) {
            saveDefaultConfig();

            loadListeners();
            loadCommands();
            loadCompleter();
        } else {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&c[" + getDescription().getName() + "] Установите плагин - Vault или PlayerPoints или TokenManager.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public static Loader getInst() {
        return inst;
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new KillListener(), this);
    }

    public void loadCommands() {
        getCommand("aqkills").setExecutor(new Commands());
    }

    public void loadCompleter() {
        getCommand("aqkills").setTabCompleter(new Commands());
    }
}
