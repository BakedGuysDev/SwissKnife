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

package com.egirlsnation.swissknife.listener.inventory;

import com.egirlsnation.swissknife.util.IllegalItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class onInventoryOpen implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    @EventHandler
    private void InventoryOpen(InventoryOpenEvent e){
        for(ItemStack item : e.getInventory().getContents()){
            if(e.getPlayer() instanceof Player){
                illegalItemHandler.handleIllegals(item, (Player) e.getPlayer());
            }else{
                illegalItemHandler.handleIllegals(item);
            }
        }
    }
}
