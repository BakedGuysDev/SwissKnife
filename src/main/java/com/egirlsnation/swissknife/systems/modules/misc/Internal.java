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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Internal extends Module {
    public Internal() {
        super(Categories.Misc, "internal", "Internal module that shouldn't be turned off");
    }

    //TODO: Move to draconite items module
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
