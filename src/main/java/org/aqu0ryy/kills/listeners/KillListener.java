package org.aqu0ryy.kills.listeners;

import org.aqu0ryy.kills.Loader;
import org.aqu0ryy.kills.utils.ChatUtil;
import org.aqu0ryy.kills.utils.VaultUtil;
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
        if (event.getEntity().getKiller() instanceof Player && event.getEntity().getKiller() != null) {
            Player victim = event.getEntity();
            Player killer = victim.getKiller();

            double money = VaultUtil.getBalance(victim) * Loader.getInst().getConfig().getDouble("balance-percent");

            if (Loader.getInst().getConfig().getInt("balance-percent") > 1 || Loader.getInst().getConfig().getInt("balance-percent") < 0) {
                ChatUtil.sendMessage(Bukkit.getConsoleSender(), Loader.getInst().getConfig().getString("error-percent"));
                Loader.getInst().getConfig().set("balance-percent", 0.05);
                Loader.getInst().saveConfig();
                Loader.getInst().reloadConfig();
            } else {
                ChatUtil.sendMessage(victim, Objects.requireNonNull(
                        Loader.getInst().getConfig().getString("for-victim.message")).replace("{killer}",
                        killer.getName()).replace("{money}", String.valueOf(Math.round(money))));
                ChatUtil.sendMessage(killer, Objects.requireNonNull(
                        Loader.getInst().getConfig().getString("for-killer.message")).replace("{victim}",
                        victim.getName()).replace("{money}", String.valueOf(Math.round(money))));
                String[] soundVictim = Objects.requireNonNull(Loader.getInst().getConfig().getString("for-victim.sound")).split(" ");
                victim.playSound(victim.getLocation(), Sound.valueOf(soundVictim[0]), Float.parseFloat(soundVictim[1]), Float.parseFloat(soundVictim[2]));
                String[] soundKiller = Objects.requireNonNull(Loader.getInst().getConfig().getString("for-killer.sound")).split(" ");
                killer.playSound(killer.getLocation(), Sound.valueOf(soundKiller[0]), Float.parseFloat(soundKiller[1]), Float.parseFloat(soundKiller[2]));

                VaultUtil.removeBalance(victim, money);
                VaultUtil.addBalance(killer, money);
            }
        }
    }
}
