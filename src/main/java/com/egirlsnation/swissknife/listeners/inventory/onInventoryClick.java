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

package com.egirlsnation.swissknife.listeners.inventory;

import com.egirlsnation.swissknife.api.IllegalItemHandler;
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