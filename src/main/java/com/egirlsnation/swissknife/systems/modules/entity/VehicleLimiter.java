/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.systems.modules.entity;

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.entity.EntityUtil;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

import java.util.Comparator;
import java.util.List;

public class VehicleLimiter extends Module {
    public VehicleLimiter(){
        super(Categories.Entity, "vehicle-limiter", "Limits how many vehicles can be in chunk");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> vehicleCount = sgGeneral.add(new IntSetting.Builder()
            .name("vehicle-count")
            .description("How many vehicles can be in a chunk")
            .defaultValue(26)
            .min(0)
            .build()
    );

    private final Setting<Boolean> sortList = sgGeneral.add(new BoolSetting.Builder()
            .name("remove-new-first")
            .description("Removes vehicles depending on age - newest to oldest (might lag a bit with huge vehicle counts)")
            .defaultValue(false)
            .build()

    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log removed vehicles")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    public void VehicleCreate(VehicleCreateEvent e){
        if(!isEnabled()) return;

        if(EntityUtil.countVehicles(e.getVehicle().getLocation().getChunk().getEntities()) > vehicleCount.get()){
            e.setCancelled(true);
            if(log.get()){
                info("Cancelled creating vehicle in chunk over vehicle limit at: " + LocationUtil.getLocationString(e.getVehicle().getLocation()));
            }
        }
    }

    @EventHandler
    public void VehicleCollision(VehicleEntityCollisionEvent e){
        if(!isEnabled()) return;

        List<Entity> vehicles = EntityUtil.filterVehicles(e.getVehicle().getLocation().getChunk().getEntities());
        if(vehicles.size() > vehicleCount.get()){
            removeExcessVehicles(vehicles);
            info("Removed vehicles over chunk limit at: " + LocationUtil.getLocationString(e.getVehicle().getLocation()));
        }
    }

    private void removeExcessVehicles(List<Entity> vehicleList){
        if(vehicleList.isEmpty() || vehicleList.size() < vehicleCount.get()){
            return;
        }

        if(sortList.get()){
            vehicleList.sort(Comparator.comparing(Entity::getTicksLived));
        }

        int i = vehicleList.size();
        for(Entity entity : vehicleList){
            if(i > vehicleCount.get()){
                entity.remove();
                i--;
            }
        }
    }
}
