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

import com.egirlsnation.swissknife.utils.EntityUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class onXpBottle implements Listener {

    private final EntityUtil entityUtil = new EntityUtil();

    @EventHandler
    public void xpBottleListener(ExpBottleEvent e){
        if(!preventXpBottleLag) return;
        if(entityUtil.countEntities(EntityType.THROWN_EXP_BOTTLE, e.getEntity().getLocation().getChunk().getEntities()) > xpBottleLimit){
            e.setCancelled(true);
            e.getEntity().remove();
        }
    }
}
