package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.event.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onPlayerPlaceCrystal implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @EventHandler
    private void PlayerPlaceCrystal(PlayerPlaceCrystalEvent e){
        if(!customItemHandler.isDraconiteCrystal(e.getCrystalItem())) return;
        Bukkit.getLogger().info("Draconite crystal placed");
        e.getCrystal().setCustomName("Draconite Crystal");
    }
}
