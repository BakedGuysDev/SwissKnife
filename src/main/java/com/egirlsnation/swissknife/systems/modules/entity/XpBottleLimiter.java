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
import com.egirlsnation.swissknife.utils.EntityUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExpBottleEvent;

public class XpBottleLimiter extends Module {
    public XpBottleLimiter() {
        super(Categories.Entity,"xp-bottle-limiter", "Limits how many xp bottles can be in a chunk");
    }


    @EventHandler
    public void xpBottleListener(ExpBottleEvent e){
        if(!isEnabled()) return;
        if(!Config.instance.preventXpBottleLag) return;
        if(EntityUtil.countEntities(EntityType.THROWN_EXP_BOTTLE, e.getEntity().getLocation().getChunk().getEntities()) > Config.instance.xpBottleLimit){
            e.setCancelled(true);
            e.getEntity().remove();
        }
    }
}
