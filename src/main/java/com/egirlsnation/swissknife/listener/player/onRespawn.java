package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.util.RespawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.mainWorldName;

public class onRespawn implements Listener {

    private final RespawnHandler respawnHandler = new RespawnHandler();

    @EventHandler
    private void Respawn(PlayerRespawnEvent e){
        if(e.getPlayer().getBedSpawnLocation() != null) return;
        e.setRespawnLocation(respawnHandler.getRandomLocation(Bukkit.getWorld(mainWorldName)));
    }
}
