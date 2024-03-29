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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SmallFixes extends Module {
    public SmallFixes(){
        super(Categories.Misc, "small-fixes", "Small fixes that don't need module on it's own");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> ownVehicleDamage = sgGeneral.add(new BoolSetting.Builder()
            .name("own-vehicle-damage")
            .description("Prevents player from hitting their own vehicle")
            .defaultValue(true)
            .build()
    );


    @EventHandler
    private void entityDamageEntity(EntityDamageByEntityEvent e){
        if(!isEnabled()) return;
        if(!ownVehicleDamage.get()) return;
        if(e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            if(e.getEntity().getPassengers().contains(player)){
                e.setCancelled(true);
            }
        }
    }

    private final Setting<Boolean> disableShulkerSpill = sgGeneral.add(new BoolSetting.Builder()
            .name("disable-shulker-spill")
            .description("Prevents items from shulkers being dropped to the ground when shulker is destroyed")
            .defaultValue(true)
            .build()
    );


    @EventHandler
    private void entityDamage(EntityDamageEvent e){
        if(!isEnabled()) return;
        if(!disableShulkerSpill.get()) return;
        if(e.getEntity() instanceof Item){
            Item item = (Item) e.getEntity();
            if(ItemUtil.isShulkerBox(item.getItemStack())){
                e.setCancelled(true);
                item.remove();
            }
        }
    }
}
