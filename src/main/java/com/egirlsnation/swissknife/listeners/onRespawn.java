package com.egirlsnation.swissknife.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.egirlsnation.swissknife.service.respawnLocationService.randomLocation;

public class onRespawn implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        World world = player.getWorld();
        if(player.getBedSpawnLocation() == null){
            if(!world.getName().contains("nether") && !world.getName().contains("end")){
                Location location = randomLocation(player.getWorld());
                e.setRespawnLocation(location);
            }
        }
    }
}
