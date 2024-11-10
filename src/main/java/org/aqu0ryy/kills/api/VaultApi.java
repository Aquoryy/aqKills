package org.aqu0ryy.kills.api;

import net.milkbowl.vault.economy.Economy;
import org.aqu0ryy.kills.Loader;
import org.aqu0ryy.kills.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultApi {

    private static Economy economy = null;

    public static boolean setupVault() {
        if (Loader.getInst().getServer().getPluginManager().getPlugin("Vault") == null) {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&c[" + Loader.getInst().getDescription().getName() + "] Не установлен Vault, опция этой экономики недоступна.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = Loader.getInst().getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

    public static int getBalance(OfflinePlayer player) {
        return (int) economy.getBalance(player);
    }

    public static void removeBalance(OfflinePlayer player, double money) {
        economy.withdrawPlayer(player, money);
    }

    public static void addBalance(OfflinePlayer player, double money) {
        economy.depositPlayer(player, money);
    }
}
