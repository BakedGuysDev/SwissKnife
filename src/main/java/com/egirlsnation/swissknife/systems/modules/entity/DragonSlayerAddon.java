/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
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
import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class DragonSlayerAddon extends Module {
    public DragonSlayerAddon() {
        super(Categories.Entity,"dragonslayer-addon", "Implements some fixes for the dragonslayer plugin");
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e){
        if(!isEnabled()) return;
        if(e.getEntity().getType().equals(EntityType.ENDER_DRAGON)){
            if((e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))){
                LivingEntity dragon = (LivingEntity) e.getEntity();
                if(dragon.getHealth() > OldConfig.instance.dragonHealth) return;
                e.setCancelled(true);
            }
        }
    }
}
