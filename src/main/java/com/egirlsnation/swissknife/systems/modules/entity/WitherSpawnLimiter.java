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

import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class WitherSpawnLimiter extends Module {

    public WitherSpawnLimiter() {
        super("wither-spawn-limiter", "Limits spawning withers at spawn");
    }

    @EventHandler
    private void EntitySpawn(CreatureSpawnEvent e){

        //Limit wither spawning at spawn
        if(!Config.instance.preventWithersAtSpawn) return;

        if(e.getEntityType() == EntityType.WITHER){
            if(LocationUtil.isInSpawnRadius(e.getLocation().getX(),e.getLocation().getZ(), Config.instance.spawnRadius)){
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