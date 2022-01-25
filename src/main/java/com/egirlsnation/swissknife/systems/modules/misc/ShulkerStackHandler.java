/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ShulkerStackHandler extends Module {
    public ShulkerStackHandler() {
        super(Categories.Misc, "stacks-in-shulkers", "Sets itemstacks to their max stack size when closing a shulker to prevent NBT bans and kicks (might not be needed on some versions)");
    }

    @EventHandler
    private void InventoryClose(InventoryCloseEvent e){
        if(!isEnabled()) return;
        if(!Config.instance.unstackInShulks) return;
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
