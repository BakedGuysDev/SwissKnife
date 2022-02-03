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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.entity.EntityUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class onVehicleCreate implements Listener {

    private final EntityUtil entityUtil = new EntityUtil();

    @EventHandler
    public void VehicleCreate(VehicleCreateEvent e){

        //Limits the number of vehicles in chunk
        if(OldConfig.instance.limitVehicles){
            if(entityUtil.countVehicles(e.getVehicle().getLocation().getChunk().getEntities()) > OldConfig.instance.vehicleLimitChunk){
                e.setCancelled(true);
            }
        }
    }
}
