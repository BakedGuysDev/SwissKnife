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

package com.egirlsnation.swissknife.listeners.block;

import com.egirlsnation.swissknife.api.IllegalItemHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class onBlockDispense implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    private void onBlockDispenseEvent(BlockDispenseEvent e){

        if(illegalItemHandler.isOverEnchanted(e.getItem())) {
            if(e.getItem().getType().equals(Material.NETHERITE_SWORD)) return;
            if(e.getItem().getType().equals(Material.NETHERITE_AXE)) return;
            e.setItem(illegalItemHandler.getIllegalItemReplacement());
            return;
        }

        if(illegalItemHandler.isSpawnEgg(e.getItem())){
            e.setItem(illegalItemHandler.getIllegalItemReplacement());
            return;
        }
    }
}
