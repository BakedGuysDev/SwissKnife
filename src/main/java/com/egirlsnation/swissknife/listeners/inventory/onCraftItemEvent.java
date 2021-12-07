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

import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class onCraftItemEvent implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @EventHandler
    public void CraftItem(CraftItemEvent e) {
        if(e.getInventory().getResult() == null) return;
        if (!(e.getView().getPlayer() instanceof Player)) return;
        if (!customItemHandler.isDraconitePickaxe(e.getInventory().getResult())) return;

        if (!Config.instance.enablePickaxeCraft) {
            e.setCancelled(true);
            return;
        }

        if (!Config.instance.useDraconiteGems) return;
        int gems = 0;

        for (ItemStack item : e.getInventory().getContents()) {
            if (customItemHandler.isDraconiteGem(item)) {
                gems++;
            }
        }
        if (gems != 2){
            e.setCancelled(true);
            e.getInventory().getResult().setAmount(0);
        }
    }
}
