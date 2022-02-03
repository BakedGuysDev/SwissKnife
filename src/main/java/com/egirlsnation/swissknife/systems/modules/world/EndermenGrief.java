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

package com.egirlsnation.swissknife.systems.modules.world;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EndermenGrief extends Module {
    public EndermenGrief() {
        super(Categories.World, "endermen-grief", "Disables endermen picking up blocks in the end");
    }

    @EventHandler
    private void EntityChangeBlock(EntityChangeBlockEvent e){
        if(!isEnabled()) return;

        if(!e.getEntity().getType().equals(EntityType.ENDERMAN)) return;
        if(!e.getBlock().getLocation().getWorld().getEnvironment().equals(World.Environment.THE_END)) return;
        e.setCancelled(true);
    }
}
