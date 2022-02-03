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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class onEntityChangeBlock implements Listener {

    @EventHandler
    private void EntityChangeBlock(EntityChangeBlockEvent e){
        if(!OldConfig.instance.disableEndermanGrief) return;
        if(!e.getEntity().getType().equals(EntityType.ENDERMAN)) return;
        if(!e.getBlock().getLocation().getWorld().getName().equals(OldConfig.instance.endWorldName)) return;
        e.setCancelled(true);
    }
}
