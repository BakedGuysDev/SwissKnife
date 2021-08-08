package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.EntityUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCollisionEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.limitVehicles;
import static com.egirlsnation.swissknife.SwissKnife.Config.vehicleLimitChunk;

public class onVehicleCollision implements Listener {

    private final EntityUtils entityUtils = new EntityUtils();

    @EventHandler
    public void VehicleCollision(VehicleCollisionEvent e){
        //Limits the number of vehicles in chunk
        if(limitVehicles){
            Entity[] vehicles = e.getVehicle().getLocation().getChunk().getEntities();
            if(entityUtils.countVehicles(vehicles) > vehicleLimitChunk){
                entityUtils.removeExcessVehicles(entityUtils.filterVehicles(vehicles));
            }
        }
    }
}
