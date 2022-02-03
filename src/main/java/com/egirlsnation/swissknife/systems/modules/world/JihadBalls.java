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

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

public class JihadBalls extends Module {
    public JihadBalls() {
        super(Categories.World, "jihad-balls", "Makes snowballs explode");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> limitRadius = sgGeneral.add(new BoolSetting.Builder()
            .name("limit-radius")
            .description("If jihad balls should be limited by spawn radius")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
            .name("radius")
            .description("Radius from spawn to allow use of jihads in")
            .defaultValue(100)
            .build()
    );

    private final Setting<Boolean> invertRadius = sgGeneral.add(new BoolSetting.Builder()
            .name("invert-radius")
            .description("Instead of allowing jihads to be used in spawn radius they will be allowed outside")
            .defaultValue(false)
            .build()
    );

    private final Setting<Integer> strength = sgGeneral.add(new IntSetting.Builder()
            .name("explosion-strength")
            .description("How strong will the explosion be. (Be careful with this)")
            .defaultValue(6)
            .min(1)
            .build()
    );

    private final Setting<Boolean> fire = sgGeneral.add(new BoolSetting.Builder()
            .name("fire")
            .description("If the explosion should set blocks on fire")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> breakBlocks = sgGeneral.add(new BoolSetting.Builder()
            .name("break-blocks")
            .description("If the explosion should break blocks or only deal damage")
            .defaultValue(true)
            .build()
    );

    @EventHandler
    private void ProjectileHit(ProjectileHitEvent e) {
        if(!isEnabled()) return;

        if (e.getEntityType().equals(EntityType.SNOWBALL)) {

            if(limitRadius.get()){
                double x = e.getEntity().getLocation().getX();
                double z = e.getEntity().getLocation().getZ();
                if(invertRadius.get() && !LocationUtil.isInSpawnRadius(x, z, radius.get())){
                    explodeJihad(e.getEntity());
                }else if(!invertRadius.get() && LocationUtil.isInSpawnRadius(x, z, radius.get())){
                    explodeJihad(e.getEntity());
                }
            }else{
                explodeJihad(e.getEntity());
            }
        }
    }

    private void explodeJihad(Entity entity){
        entity.getWorld().createExplosion(entity.getLocation(), (float) strength.get(), fire.get(), breakBlocks.get(), entity);
    }
}
