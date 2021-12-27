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

package com.egirlsnation.swissknife.systems.modules;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Internal extends Module {
    public Internal() {
        super("internal", "Internal module that shouldn't be turned off");
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent e){
        // Prevents players nametagging crystals (needed for draconite crystals)
        if(e.getRightClicked().getType().equals(EntityType.ENDER_CRYSTAL)){
            if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)){
                e.setCancelled(true);
                return;
            }
            if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.NAME_TAG)){
                e.setCancelled(true);
            }
        }
    }
}