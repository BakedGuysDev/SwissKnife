package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.swissKnife;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onQuit implements Listener {

    public swissKnife plugin;
    public onQuit(swissKnife instance){
        plugin = instance;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(plugin.SQL.isConnected()){
            plugin.sqlService.updateValues(e.getPlayer());
        }
    }
}
