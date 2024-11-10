package org.aqu0ryy.kills.api;

import me.realized.tokenmanager.api.TokenManager;
import org.aqu0ryy.kills.Loader;
import org.aqu0ryy.kills.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.OptionalLong;

public class TokenManagerApi {

    private static TokenManager tokenManager;

    public static boolean setupTokenManager() {
        if (Loader.getInst().getServer().getPluginManager().getPlugin("TokenManager") == null) {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&c[" + Loader.getInst().getDescription().getName() + "] Не установлен TokenManager, опция этой экономики недоступна.");
            return false;
        }

        return true;
    }

    public static OptionalLong getBalance(Player player) {
        return tokenManager.getTokens(player);
    }

    public static void removeBalance(Player player, long money) {
        tokenManager.removeTokens(player, money);
    }

    public static void addBalance(Player player, long money) {
        tokenManager.addTokens(player, money);
    }
}
