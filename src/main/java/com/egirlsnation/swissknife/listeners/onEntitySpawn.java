package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.service.radiusManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;

public class onEntitySpawn implements Listener {

    radiusManager radiusManager = new radiusManager();

    @EventHandler
    public void EntitySpawn (CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BEEHIVE) {
            ArrayList<Entity> entitiesArray = new ArrayList<>();
            Chunk chunk = e.getLocation().getChunk();
            for (Entity entities : chunk.getEntities()) {
                if (entities.getType().equals(EntityType.BEE)) {
                    entitiesArray.add(entities);
                }
            }
            if (entitiesArray.size() > 24) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.BEE)) {
                        entity.remove();
                    }
                }
            }
            //e.setCancelled(true);
        }
        if (e.getEntity().getType() == EntityType.WITHER) {
            if (!radiusManager.isInRadius(e.getLocation().getX(), e.getLocation().getZ(), 500)) {
                e.setCancelled(true);
            }
        }

        /*if(e.getEntity().getType() == EntityType.ENDER_DRAGON){
            Block crystal1 = new Location(e.getLocation().getWorld(), -3, 58, 0).getBlock();
            Block crystal2 = new Location(e.getLocation().getWorld(), 0, 58, 3).getBlock();
            Block crystal3 = new Location(e.getLocation().getWorld(), 0, 58, -3).getBlock();
            Block crystal4 = new Location(e.getLocation().getWorld(), 3, 58, 0).getBlock();

            if((crystal1.getType() == Material.END_CRYSTAL) && (crystal2.getType() == Material.END_CRYSTAL) && (crystal3.getType() == Material.END_CRYSTAL) && (crystal4.getType() == Material.END_CRYSTAL)){
                e.setCancelled(true);
            }

        }*/
    }
}
