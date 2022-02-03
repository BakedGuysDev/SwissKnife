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

package com.egirlsnation.swissknife.utils.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {

    public static Integer countVehicles(Entity[] entities){
        int count = 0;
        for(Entity entity : entities){
            if(entity instanceof Vehicle){
               count++;
            }
        }
        return count;
    }

    public static List<Entity> filterVehicles(Entity[] entities){
        List<Entity> vehicles = new ArrayList<>();
        for(Entity entity : entities){
            if(entity instanceof Vehicle && entity.getPassengers().stream().noneMatch(e -> e instanceof Player)){
                vehicles.add(entity);
            }
        }
        return vehicles;
    }

    public static List<Entity> filterEntityType(Entity[] entities, EntityType typeToFilter){
        List<Entity> filtered = new ArrayList<>();
        for(Entity entity : entities){
            if(entity.getType().equals(typeToFilter)){
                filtered.add(entity);
            }
        }
        return filtered;
    }

    public static int countEntities(EntityType entityType, Entity[] entities){
        int count = 0;
        for(Entity entity : entities){
            if(entity.getType().equals(entityType)){
                count++;
            }
        }
        return count;
    }
}
