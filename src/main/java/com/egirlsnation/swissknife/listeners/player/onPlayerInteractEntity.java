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

package com.egirlsnation.swissknife.listeners.player;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class onPlayerInteractEntity implements Listener {

    @EventHandler
    private void PlayerInteractAtEntity(PlayerInteractEntityEvent e){
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
