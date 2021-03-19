package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class entityPortalTeleportEvent implements Listener {

    World overworld = Bukkit.getWorld("world");
    World nether = Bukkit.getWorld("world_nether");

    @EventHandler
    public void onEntityPortalTeleport (EntityPortalEvent e){
        World world = e.getTo().getWorld();
        if(e.getEntityType() == EntityType.BEE){
                e.setCancelled(true);
        }
        if(!e.getFrom().getWorld().equals(world)){
            if(e.getEntityType().equals(EntityType.ENDER_CRYSTAL)){
                e.setCancelled(true);
            }
        }
    }
}
