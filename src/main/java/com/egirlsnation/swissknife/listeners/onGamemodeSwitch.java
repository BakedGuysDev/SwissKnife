package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class onGamemodeSwitch implements Listener {

    private static final swissKnife swissknife = swissKnife.getInstance();

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent e){
        final Player player = e.getPlayer();
        if(!player.hasPermission("essentials.fly")){
            new BukkitRunnable() {

                @Override
                public void run() {
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String playerName = player.getName();
                    String command = "fly " + playerName + " disable";
                    Bukkit.dispatchCommand(console, command);
                }

            }.runTaskLater(swissknife, 5);
        }
    }
}
