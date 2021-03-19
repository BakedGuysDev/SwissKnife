package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class onPlayerTeleport implements Listener {

    public void onPlayerTeleportEvent (PlayerTeleportEvent e){
        Bukkit.getLogger().info(ChatColor.RED + "Player Teleport event was triggered.");
        if(e.getPlayer().isInsideVehicle()){
            Bukkit.getLogger().info(ChatColor.RED + "Player Teleport event was triggered. And player is inside vehicle.");
            Entity vehicle = e.getPlayer().getVehicle();
            vehicle.teleport(e.getTo());
        }
    }
}
