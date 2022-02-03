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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.systems.handlers.CombatCheckHandler;
import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class onEntityToggleGlide implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();
    private final CombatCheckHandler combatCheckHandler = new CombatCheckHandler();

    @EventHandler
    public void EntityToggleGlide(EntityToggleGlideEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if(customItemHandler.getCrystalEnabledList().contains(player.getUniqueId()) || combatCheckHandler.getElytraMap().containsKey(player.getUniqueId())){
            e.setCancelled(true);
        }
    }
}
