package com.egirlsnation.swissknife.listener.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.endWorldName;

public class onEntityChangeBlock implements Listener {

    @EventHandler
    private void EntityChangeBlock(EntityChangeBlockEvent e){
        if(!e.getEntity().getType().equals(EntityType.ENDERMAN)) return;
        if(!e.getBlock().getLocation().getWorld().getName().equals(endWorldName)) return;
        e.setCancelled(true);
    }
}
