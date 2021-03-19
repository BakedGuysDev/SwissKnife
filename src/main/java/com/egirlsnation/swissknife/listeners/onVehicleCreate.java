package com.egirlsnation.swissknife.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.ArrayList;

public class onVehicleCreate implements Listener {

    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent e){

            ArrayList<Entity> entitiesArray = new ArrayList<>();
            Chunk chunk = e.getVehicle().getLocation().getChunk();
            for (Entity entities : chunk.getEntities()) {
                if (entities.getType().equals(EntityType.MINECART) ||
                        entities.getType().equals(EntityType.MINECART_CHEST) ||
                        entities.getType().equals(EntityType.MINECART_TNT) ||
                        entities.getType().equals(EntityType.MINECART_FURNACE) ||
                        entities.getType().equals(EntityType.MINECART_HOPPER)) {
                    entitiesArray.add(entities);
                }
            }
            if (entitiesArray.size() > 40) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.MINECART) ||
                            entity.getType().equals(EntityType.MINECART_CHEST) ||
                            entity.getType().equals(EntityType.MINECART_TNT) ||
                            entity.getType().equals(EntityType.MINECART_FURNACE) ||
                            entity.getType().equals(EntityType.MINECART_HOPPER)) {
                        entity.remove();
                    }
                }
            }
            //e.setCancelled(true);
    }
}
