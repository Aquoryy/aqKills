package org.aqu0ryy.kills.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.aqu0ryy.kills.Loader;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtil {

    private static Economy economy = null;

    public static boolean setupEconomy() {
        if (Loader.getInst().getServer().getPluginManager().getPlugin("Vault") == null) {
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

    public static EconomyResponse removeBalance(OfflinePlayer player, double money) {
        return economy.withdrawPlayer(player, money);
    }

    public static EconomyResponse addBalance(OfflinePlayer player, double money) {
        return economy.depositPlayer(player, money);
    }
}
