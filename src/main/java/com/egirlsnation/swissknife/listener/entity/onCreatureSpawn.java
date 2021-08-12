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
import com.egirlsnation.swissknife.util.SpawnRadiusManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.spawnRadius;

public class onCreatureSpawn implements Listener {


    private final SpawnRadiusManager radiusManager = new SpawnRadiusManager();
    private final EntityUtils eUtils = new EntityUtils();
    @EventHandler
    private void EntitySpawn(CreatureSpawnEvent e){

        //Limit wither spawning at spawn
        if(e.getEntityType() == EntityType.WITHER){
            if(radiusManager.isInRadius(e.getLocation().getX(), e.getLocation().getZ(), spawnRadius)){
                e.setCancelled(true);
                for(Entity entity : e.getEntity().getNearbyEntities(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ())){
                    if(entity instanceof Player){
                        entity.sendMessage(ChatColor.RED + "You cannot spawn withers this close to spawn.");
                    }
                }
            }
        }
    }
}
