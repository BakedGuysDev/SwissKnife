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

package com.egirlsnation.swissknife.systems.modules.world;

import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EndermenGrief extends Module {
    public EndermenGrief() {
        super("endermen-grief", "Disables endermen picking up blocks in the end");
    }

    @EventHandler
    private void EntityChangeBlock(EntityChangeBlockEvent e){
        if(!Config.instance.disableEndermanGrief) return;
        if(!e.getEntity().getType().equals(EntityType.ENDERMAN)) return;
        // TODO: Better way than world name
        if(!e.getBlock().getLocation().getWorld().getName().equals(Config.instance.endWorldName)) return;
        e.setCancelled(true);
    }
}
