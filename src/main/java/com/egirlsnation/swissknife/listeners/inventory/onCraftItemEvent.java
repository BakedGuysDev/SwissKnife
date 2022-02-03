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

import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import com.egirlsnation.swissknife.utils.OldConfig;
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

        if (!OldConfig.instance.enablePickaxeCraft) {
            e.setCancelled(true);
            return;
        }

        if (!OldConfig.instance.useDraconiteGems) return;
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