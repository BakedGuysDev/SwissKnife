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

package com.egirlsnation.swissknife.systems.modules.egirls;

import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class DragonSlayerAddon extends Module {
    public DragonSlayerAddon() {
        super(Categories.Entity,"dragon-slayer-addon", "Implements some fixes for the dragonslayer plugin");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> health = sgGeneral.add(new IntSetting.Builder()
            .name("health")
            .defaultValue(100)
            .build()
    );

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e){
        if(!isEnabled()) return;
        if(e.getEntity().getType().equals(EntityType.ENDER_DRAGON)){
            if((e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))){
                LivingEntity dragon = (LivingEntity) e.getEntity();
                if(dragon.getHealth() > health.get()) return;
                e.setCancelled(true);
            }
        }
    }
}
