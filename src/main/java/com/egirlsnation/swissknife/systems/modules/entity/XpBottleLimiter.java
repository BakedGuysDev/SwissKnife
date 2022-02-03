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

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.entity.EntityUtil;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.List;

public class XpBottleLimiter extends Module {
    public XpBottleLimiter() {
        super(Categories.Entity,"xp-bottle-limiter", "Limits how many xp bottles can be in a chunk");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> xpBottleCount = sgGeneral.add(new IntSetting.Builder()
            .name("bottle-count")
            .description("How many xp bottles there can be in a chunk")
            .defaultValue(64)
            .min(0)
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log removing xp bottles over the limit")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    public void xpBottleListener(ExpBottleEvent e){
        if(!isEnabled()) return;

        if(EntityUtil.countEntities(EntityType.THROWN_EXP_BOTTLE, e.getEntity().getLocation().getChunk().getEntities()) > xpBottleCount.get()){
            e.setCancelled(true);
            e.getEntity().remove();
            if(log.get()){
                info("Removed xp bottle over limit at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
            }
        }
    }

    @EventHandler
    public void projectileLaunchListener(ProjectileLaunchEvent e){
        if(!isEnabled()) return;
        if(e.getEntity().getType().equals(EntityType.THROWN_EXP_BOTTLE)){
            List<Entity> entities = EntityUtil.filterEntityType(e.getEntity().getChunk().getEntities(), EntityType.THROWN_EXP_BOTTLE);
            if(entities.size() > xpBottleCount.get()){
                removeExcessBottles(entities);
                if(log.get()){
                    info("Removed xp bottles over limit at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
                }
            }
        }
    }

    private void removeExcessBottles(List<Entity> bottles){
        if(bottles.isEmpty() || bottles.size() < xpBottleCount.get()){
            return;
        }

        int i = bottles.size();
        for(Entity bottle : bottles){
            if(i > xpBottleCount.get()){
                bottle.remove();
                i--;
            }
        }
    }
}
