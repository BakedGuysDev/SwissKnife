package com.egirlsnation.swissknife.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;

public class onChunkLoad implements Listener {

    @EventHandler
    public void ChunkLoadEvent (ChunkLoadEvent e){
        Chunk chunk = e.getChunk();
        ArrayList<Entity> entitiesArray = new ArrayList<Entity>();
        for(Entity entities : chunk.getEntities()){
            if(entities.getType().equals(EntityType.BEE)){
                entitiesArray.add(entities);
            }
        }
        if(entitiesArray.size() > 24){
            for(Entity entity : chunk.getEntities()){
                if(entity.getType().equals(EntityType.BEE)){
                    entity.remove();
                }
            }
        }

    }
}
