package org.aqu0ryy.kills.listeners;

import org.aqu0ryy.kills.Loader;
import org.aqu0ryy.kills.api.PlayerPointsApi;
import org.aqu0ryy.kills.api.TokenManagerApi;
import org.aqu0ryy.kills.api.VaultApi;
import org.aqu0ryy.kills.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class KillListener implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            double money = calculateMoney(victim);

            if (money >= 0) {
                sendNotify(victim, killer, money);
                updateBalances(victim, killer, money);
            }
        }
    }

    private double calculateMoney(Player victim) {
        double balancePercent = Loader.getInst().getConfig().getDouble("balance-percent");
        if (balancePercent > 1 || balancePercent < 0) {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), Loader.getInst().getConfig().getString("error-percent"));
            return -1;
        }

        String economy = Loader.getInst().getConfig().getString("economy");

        if (economy != null) {
            if (VaultApi.setupVault() && "vault".equals(economy)) {
                return VaultApi.getBalance(victim) * balancePercent;
            } else if (PlayerPointsApi.setupPlayerPoints() && "playerpoints".equals(economy)) {
                return PlayerPointsApi.getBalance(victim) * balancePercent;
            } else if (TokenManagerApi.setupTokenManager() && "tokenmanager".equals(economy)) {
                return TokenManagerApi.getBalance(victim).orElse(0L) * balancePercent;
            } else {
                ChatUtil.sendMessage(Bukkit.getConsoleSender(), Objects.requireNonNull(Loader.getInst().getConfig().getString("error-economy")).replace("{economy}", economy));
            }
        }
        return -1;
    }

    private void sendNotify(Player victim, Player killer, double money) {
        String victimMessage = Objects.requireNonNull(
                Loader.getInst().getConfig().getString("for-victim.message")).replace("{killer}",
                killer.getName()).replace("{money}", String.valueOf(Math.round(money)));
        String killerMessage = Objects.requireNonNull(
                Loader.getInst().getConfig().getString("for-killer.message")).replace("{victim}",
                victim.getName()).replace("{money}", String.valueOf(Math.round(money)));

        ChatUtil.sendMessage(victim, victimMessage);
        ChatUtil.sendMessage(killer, killerMessage);

        playSound(victim, "for-victim.sound");
        playSound(killer, "for-killer.sound");
    }

    private void playSound(Player player, String sound) {
        String[] soundConfig = Objects.requireNonNull(Loader.getInst().getConfig().getString(sound)).split(" ");
        player.playSound(player.getLocation(), Sound.valueOf(soundConfig[0]), Float.parseFloat(soundConfig[1]), Float.parseFloat(soundConfig[2]));
    }

    private void updateBalances(Player victim, Player killer, double money) {
        String economy = Loader.getInst().getConfig().getString("economy");

        if (VaultApi.setupVault() && "vault".equals(economy)) {
            VaultApi.removeBalance(victim, money);
            VaultApi.addBalance(killer, money);
        } else if (PlayerPointsApi.setupPlayerPoints() && "playerpoints".equals(economy)) {
            PlayerPointsApi.removeBalance(victim, (int) money);
            PlayerPointsApi.addBalance(killer, (int) money);
        } else if (TokenManagerApi.setupTokenManager() && "tokenmanager".equals(economy)) {
            TokenManagerApi.removeBalance(victim, (long) money);
            TokenManagerApi.addBalance(killer, (long) money);
        }
    }
}