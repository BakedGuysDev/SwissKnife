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

package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.EntityUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.limitVehicles;
import static com.egirlsnation.swissknife.SwissKnife.Config.vehicleLimitChunk;

public class onVehicleCreate implements Listener {

    private final EntityUtils entityUtils = new EntityUtils();

    @EventHandler
    public void VehicleCreate(VehicleCreateEvent e){

        //Limits the number of vehicles in chunk
        if(limitVehicles){
            if(entityUtils.countVehicles(e.getVehicle().getLocation().getChunk().getEntities()) > vehicleLimitChunk){
                e.setCancelled(true);
            }
        }
    }
}
