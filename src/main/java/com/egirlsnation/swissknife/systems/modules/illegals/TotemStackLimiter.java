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
import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.IllegalItemsUtil;
import com.egirlsnation.swissknife.utils.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class TotemStackLimiter extends Module {
    public TotemStackLimiter() {
        super(Categories.Illegals, "totem-stack-limiter", "Limits how big illegally stacked totem stacks can be");
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        if(InventoryUtil.scanAndTrimTotemStack(e.getInventory()) && e.getPlayer() instanceof Player){
            IllegalItemsUtil.notifyPlayerAboutOSI((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getClickedInventory() == null) return;
        if(InventoryUtil.scanAndTrimTotemStack(e.getInventory()) && e.getWhoClicked() instanceof Player){
            IllegalItemsUtil.notifyPlayerAboutOSI((Player) e.getWhoClicked());
        }

    }



    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(e.getItem().getItemStack().getType().equals(Material.TOTEM_OF_UNDYING) && e.getItem().getItemStack().getAmount() > OldConfig.instance.maxTotemStack){
            e.getItem().getItemStack().setAmount(OldConfig.instance.maxTotemStack);

            if(e.getEntity() instanceof Player){
                IllegalItemsUtil.notifyPlayerAboutOSI((Player) e.getEntity());
            }
        }
    }

    @EventHandler
    private void SwapHandItems(PlayerSwapHandItemsEvent e) {
        if(!isEnabled()) return;
        if (e.getOffHandItem() != null) {
            if (e.getOffHandItem().getType().equals(Material.TOTEM_OF_UNDYING)) {
                if (e.getOffHandItem().getAmount() > OldConfig.instance.maxTotemStack) {
                    e.getOffHandItem().setAmount(OldConfig.instance.maxTotemStack);
                }
            }
        }

        if (e.getMainHandItem() != null) {
            if (e.getMainHandItem().getType().equals(Material.TOTEM_OF_UNDYING)) {
                if (e.getMainHandItem().getAmount() > OldConfig.instance.maxTotemStack) {
                    e.getMainHandItem().setAmount(OldConfig.instance.maxTotemStack);
                }
            }
        }
    }
}
