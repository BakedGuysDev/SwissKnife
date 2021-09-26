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

package com.egirlsnation.swissknife.listener.entity;

import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class onEntityToggleGlide implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @EventHandler
    public void EntityToggleGlide(EntityToggleGlideEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if(customItemHandler.getCrystalEnabledList().contains(player.getUniqueId())){
            e.setCancelled(true);
        }
    }
}
