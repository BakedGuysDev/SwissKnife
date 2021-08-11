/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class onInventoryClick implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();

    @EventHandler
    private void InventoryClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;

        if(e.getClickedInventory() == null) return;
        ItemStack[] items = e.getClickedInventory().getContents();

        for(ItemStack item : items){
            if(illegalItemHandler.handleIllegals(item, (Player) e.getWhoClicked())){
                e.setCancelled(true);
                return;
            }
        }
    }
}
