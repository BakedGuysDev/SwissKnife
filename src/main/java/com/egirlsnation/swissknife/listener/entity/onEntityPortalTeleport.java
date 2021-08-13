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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.disableEntityPortal;
import static com.egirlsnation.swissknife.SwissKnife.Config.entityTypeDisablePortal;

public class onEntityPortalTeleport implements Listener {

    @EventHandler
    private void onEntityPortalTeleportEvent(EntityPortalEvent e){
        if(!disableEntityPortal) return;
        if(entityTypeDisablePortal.contains(e.getEntityType().name())) {
            e.setCancelled(true);
        }
    }
}
