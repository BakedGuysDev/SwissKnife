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

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;

public class DisableEntityPortals extends Module {
    public DisableEntityPortals() {
        super(Categories.Entity, "disable-entity-portals", "Disables portals for certain entities");
    }

    @EventHandler
    private void onEntityPortalTeleportEvent(EntityPortalEvent e){
        if(!isEnabled()) return;
        if(!Config.instance.disableEntityPortal) return;
        if(Config.instance.entityTypeDisablePortal.contains(e.getEntityType().name())) {
            e.setCancelled(true);
        }
    }
}
