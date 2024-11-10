package org.aqu0ryy.kills.api;

import org.aqu0ryy.kills.Loader;
import org.aqu0ryy.kills.utils.ChatUtil;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerPointsApi {

    private static PlayerPointsAPI playerPoints;

    public static boolean setupPlayerPoints() {
        if (Loader.getInst().getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&c[" + Loader.getInst().getDescription().getName() + "] Не установлен PlayerPoints, опция этой экономики недоступна.");
            return false;
        } else {
            playerPoints = PlayerPoints.getInstance().getAPI();
        }

        return true;
    }

    public static int getBalance(Player player) {
        return playerPoints.look(player.getUniqueId());
    }

    public static void removeBalance(Player player, int money) {
        playerPoints.take(player.getUniqueId(), money);
    }

    public static void addBalance(Player player, int money) {
        playerPoints.give(player.getUniqueId(), money);
    }
}
