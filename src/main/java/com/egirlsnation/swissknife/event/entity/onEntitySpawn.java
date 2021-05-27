package com.egirlsnation.swissknife.event.entity;

import com.egirlsnation.swissknife.util.SpawnRadiusManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.spawnRadius;

public class onEntitySpawn implements Listener {


    private final SpawnRadiusManager radiusManager = new SpawnRadiusManager();
    @EventHandler
    public void EntitySpawn(CreatureSpawnEvent e){

        //Limit wither spawning at spawn
        if(e.getEntityType() == EntityType.WITHER){
            if(radiusManager.isInRadius(e.getLocation().getX(), e.getLocation().getZ(), spawnRadius)){
                e.setCancelled(true);
                for(Entity entity : e.getEntity().getNearbyEntities(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ())){
                    if(entity instanceof Player){
                        entity.sendMessage(ChatColor.RED + "You cannot spawn withers this close to spawn.");
                    }
                }
            }
        }


    }
}
