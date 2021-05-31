package com.egirlsnation.swissknife.listener.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class onEntityPortalTeleport implements Listener {

    @EventHandler
    private void onEntityPortalTeleportEvent(EntityPortalEvent e){
        if(e.getEntityType().equals(EntityType.BEE) || e.getEntityType().equals(EntityType.ENDER_CRYSTAL)) {
            e.setCancelled(true);
        }
    }
}
