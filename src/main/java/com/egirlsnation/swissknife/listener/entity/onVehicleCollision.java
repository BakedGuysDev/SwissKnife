package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

import java.util.List;

import static com.egirlsnation.swissknife.SwissKnife.Config.limitVehicles;
import static com.egirlsnation.swissknife.SwissKnife.Config.vehicleLimitChunk;

public class onVehicleCollision implements Listener {

    private final EntityUtils entityUtils = new EntityUtils();

    @EventHandler
    public void VehicleCollision(VehicleEntityCollisionEvent e){
        //Limits the number of vehicles in chunk
        if(limitVehicles){
            List<Entity> vehicles = entityUtils.filterVehicles(e.getVehicle().getLocation().getChunk().getEntities());
            if(vehicles.size() > vehicleLimitChunk){
                entityUtils.removeExcessVehicles(vehicles);
                Bukkit.getLogger().warning("Removed excess vehicles in chunk at: " +  e.getVehicle().getLocation().getBlockX() + " " + e.getVehicle().getLocation().getBlockY() + " " + e.getVehicle().getLocation().getBlockZ());
            }
        }
    }
}
