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

import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.IllegalItemsUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IllegalEnchantHandler extends Module {
    public IllegalEnchantHandler() {
        super("illegal-enchants", "Removes items with way too high enchant levels");
    }

    //TODO: Configurable max values for each enchant
    //TODO: Logging

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(scanAndRemoveFromInv(e.getInventory()) && e.getPlayer() instanceof Player){
            IllegalItemsUtil.notifyPlayerAboutOEI((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;
        if(scanAndRemoveFromInv(e.getInventory()) && e.getWhoClicked() instanceof Player){
            IllegalItemsUtil.notifyPlayerAboutOEI((Player) e.getWhoClicked());
        }

    }

    private boolean scanAndRemoveFromInv(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(IllegalItemsUtil.isOverEnchanted(item)){
                item.setAmount(0);
                found = true;
            }
        }
        return found;
    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(IllegalItemsUtil.isOverEnchanted(e.getItem().getItemStack())){
            e.getItem().remove();
            e.setCancelled(true);

            if(e.getEntity() instanceof Player){
                IllegalItemsUtil.notifyPlayerAboutOEI((Player) e.getEntity());
            }
        }
    }


}
