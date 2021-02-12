package com.egirlsnation.swissknife.listeners;

import net.minecraft.server.v1_16_R3.EnderDragonBattle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import com.egirlsnation.swissknife.service.WitherManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class onEntitySpawn implements Listener {

    WitherManager witherManager = new WitherManager();

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
            if (!witherManager.witherSpawningAllowed(e.getLocation().getX(), e.getLocation().getZ())) {
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
