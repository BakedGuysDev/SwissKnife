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

import com.egirlsnation.swissknife.util.CombatCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class onEntityDamageByBlock implements Listener {

    private final CombatCheck combatCheck = new CombatCheck();

    @EventHandler
    private void onEntityDamageByBlockEvent(EntityDamageByBlockEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();

        if(cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
            combatCheck.addToCombatMap(player);
        }
    }
}
