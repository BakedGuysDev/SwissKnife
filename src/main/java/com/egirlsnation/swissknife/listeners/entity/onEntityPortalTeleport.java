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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class onEntityPortalTeleport implements Listener {

    @EventHandler
    private void onEntityPortalTeleportEvent(EntityPortalEvent e){
        if(!OldConfig.instance.disableEntityPortal) return;
        if(OldConfig.instance.entityTypeDisablePortal.contains(e.getEntityType().name())) {
            e.setCancelled(true);
        }
    }
}
