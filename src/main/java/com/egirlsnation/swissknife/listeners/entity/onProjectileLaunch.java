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

import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class onProjectileLaunch implements Listener {

    private final Map<UUID, Long> throwablesMap = new HashMap<>();

    public void ProjectileLaunch(ProjectileLaunchEvent e){
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        if(e.getEntity() instanceof Snowball) return;
        Player player = (Player) e.getEntity().getShooter();
        if(Config.instance.limitThrowables){
            UUID uuid = player.getUniqueId();
            if(!throwablesMap.containsKey(uuid)){
                throwablesMap.put(uuid, System.currentTimeMillis());
            }else{
                long timeLeft = System.currentTimeMillis() - throwablesMap.get(uuid);
                if(timeLeft < Config.instance.throwablesDelay){
                    e.setCancelled(true);
                }else{
                    throwablesMap.put(uuid, System.currentTimeMillis());
                }
            }
        }
    }

}
