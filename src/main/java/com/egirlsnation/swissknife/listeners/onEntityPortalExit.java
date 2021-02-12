package com.egirlsnation.swissknife.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntityTeleportEvent;

public class onEntityPortalExit implements Listener {

    World overworld = Bukkit.getWorld("world");
    World nether = Bukkit.getWorld("world_nether");

    @EventHandler
    public void onEntityPortalExit (EntityPortalEvent e){
        if(e.getEntityType() == EntityType.BEE){
                e.setCancelled(true);
        }
    }
}
