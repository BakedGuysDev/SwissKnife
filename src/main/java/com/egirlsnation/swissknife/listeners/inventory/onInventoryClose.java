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

package com.egirlsnation.swissknife.listeners.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static com.egirlsnation.swissknife.SwissKnife.Config.unstackInShulks;

public class onInventoryClose implements Listener {

    @EventHandler
    private void InventoryClose(InventoryCloseEvent e){

        if(!unstackInShulks) return;
        if(e.getInventory().getType() != InventoryType.SHULKER_BOX) return;

        for(ItemStack item : e.getInventory().getContents()){
            if(item != null){
                if(item.getAmount() > item.getMaxStackSize()){
                    item.setAmount(item.getMaxStackSize());
                }
            }
        }
    }
}
