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
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class onProjectileHit implements Listener {

    private final LocationUtil radiusManager = new LocationUtil();

    @EventHandler
    private void ProjectileHit(ProjectileHitEvent e) {
        if (e.getEntityType().equals(EntityType.SNOWBALL)){
            if(!OldConfig.instance.jihadsEnabled) return;
            if (!radiusManager.isInRadius(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getZ(), OldConfig.instance.jihadsRadius) && OldConfig.instance.limitJihadRadius) {
                return;
            }
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) OldConfig.instance.jihadsPower, true);
            return;
        }
        if(e.getEntityType().equals(EntityType.DRAGON_FIREBALL)){
            if(e.getEntity().getCustomName() == null) return;
            if(!e.getEntity().getCustomName().equals("CusFireBallSwissKnife")) return;
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2.5f, false);
        }
    }
}
