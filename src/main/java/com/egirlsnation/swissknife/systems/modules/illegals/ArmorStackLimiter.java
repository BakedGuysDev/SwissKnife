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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.IllegalItemsUtil;
import com.egirlsnation.swissknife.utils.InventoryUtil;
import com.egirlsnation.swissknife.utils.ItemUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class ArmorStackLimiter extends Module {
    public ArmorStackLimiter() {
        super(Categories.Illegals, "armor-stack-limiter", "Limits how big can illegally stacked armor piece stacks can be");
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        if(InventoryUtil.scanAndTrimArmorStacks(e.getInventory()) && e.getPlayer() instanceof Player){
            IllegalItemsUtil.notifyPlayerAboutOSI((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getClickedInventory() == null) return;
        if(InventoryUtil.scanAndTrimArmorStacks(e.getInventory()) && e.getWhoClicked() instanceof Player){
            IllegalItemsUtil.notifyPlayerAboutOSI((Player) e.getWhoClicked());
        }

    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(ItemUtil.isArmorPiece(e.getItem().getItemStack()) && e.getItem().getItemStack().getAmount() > Config.instance.maxArmorStack){
            e.getItem().getItemStack().setAmount(Config.instance.maxArmorStack);

            if(e.getEntity() instanceof Player){
                IllegalItemsUtil.notifyPlayerAboutOSI((Player) e.getEntity());
            }
        }
    }
}
