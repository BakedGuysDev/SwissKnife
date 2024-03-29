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

package com.egirlsnation.swissknife.systems.modules.world;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ShulkerStackHandler extends Module {
    public ShulkerStackHandler() {
        super(Categories.World, "stacks-in-shulkers", "Sets itemstacks to their max stack size when closing a shulker to prevent NBT bans and kicks (might not be needed on some versions)");
    }

    @EventHandler
    private void InventoryClose(InventoryCloseEvent e){
        if(!isEnabled()) return;

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
