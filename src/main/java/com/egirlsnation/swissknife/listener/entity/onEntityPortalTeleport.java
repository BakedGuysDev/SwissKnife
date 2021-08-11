/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
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

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class onEntityPortalTeleport implements Listener {

    @EventHandler
    private void onEntityPortalTeleportEvent(EntityPortalEvent e){
        if(e.getEntityType().equals(EntityType.BEE) || e.getEntityType().equals(EntityType.ENDER_CRYSTAL)) {
            e.setCancelled(true);
        }
    }
}
