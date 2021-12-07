/*
 *
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 *
 */

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class onVehicleExitEvent implements Listener {

    @EventHandler
    public void VehicleExit(VehicleExitEvent e){
        if(Config.instance.preventPlayersOnNether && e.getExited() instanceof Player){
            Location l = e.getVehicle().getLocation();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if(l.getBlockY() >= Config.instance.netherRoofHeight && !e.getExited().isOp()){
                e.setCancelled(true);
                e.getVehicle().remove();
            }
        }
    }
}
