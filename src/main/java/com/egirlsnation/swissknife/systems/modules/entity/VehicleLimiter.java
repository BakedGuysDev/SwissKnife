/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.systems.modules.entity;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

import java.util.List;

public class VehicleLimiter extends Module {
    public VehicleLimiter() {
        super(Categories.Entity,"vehicle-limiter", "Limits how many vehicles can be in chunk");
    }

    @EventHandler
    public void VehicleCreate(VehicleCreateEvent e){

        //Limits the number of vehicles in chunk
        if(Config.instance.limitVehicles){
            if(EntityUtil.countVehicles(e.getVehicle().getLocation().getChunk().getEntities()) > Config.instance.vehicleLimitChunk){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void VehicleCollision(VehicleEntityCollisionEvent e){
        //Limits the number of vehicles in chunk
        if(Config.instance.limitVehicles){
            List<Entity> vehicles = EntityUtil.filterVehicles(e.getVehicle().getLocation().getChunk().getEntities());
            if(vehicles.size() > Config.instance.vehicleLimitChunk){
                EntityUtil.removeExcessVehicles(vehicles);
                Bukkit.getLogger().warning("Removed excess vehicles in chunk at: " +  e.getVehicle().getLocation().getBlockX() + " " + e.getVehicle().getLocation().getBlockY() + " " + e.getVehicle().getLocation().getBlockZ());
            }
        }
    }
}
