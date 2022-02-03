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

package com.egirlsnation.swissknife.listeners.block;

import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlace implements Listener {

    @EventHandler
    private void BlockPlace(BlockPlaceEvent e){
        OldConfig.instance.illegalBlockList.forEach(block -> {
            if(e.getBlock().getType().equals(Material.getMaterial(block))){
                if(e.getItemInHand().getType().equals(Material.getMaterial(block))){
                    e.setCancelled(true);
                }
            }
        });
    }
}
