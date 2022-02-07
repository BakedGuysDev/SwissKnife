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

package com.egirlsnation.swissknife.systems.modules.misc;

import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemNameLimiter extends Module {
    public ItemNameLimiter(){
        super(Categories.Misc, "item-name-limiter", "Limits how many characters there can be in an item name");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> limit = sgGeneral.add(new IntSetting.Builder()
            .name("character-limit")
            .description("How many characters can an item name have (without color codes)")
            .defaultValue(50)
            .min(1)
            .build()
    );


    @EventHandler
    private void inventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getClickedInventory() == null) return;
        scanAndTrimNames(e.getClickedInventory());
    }

    @EventHandler
    private void inventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        scanAndTrimNames(e.getInventory());
    }

    @EventHandler
    private void itemPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        ItemStack is = e.getItem().getItemStack();
        if(!is.hasItemMeta()) return;
        if(hasTooLongName(is.getItemMeta())){
            is.setItemMeta(trimName(is.getItemMeta()));
        }
        e.getItem().setItemStack(is);
    }

    public boolean scanAndTrimNames(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(item == null || !item.hasItemMeta()) continue;
            if(hasTooLongName(item.getItemMeta())){
                item.setItemMeta(trimName(item.getItemMeta()));
                found = true;
            }
        }
        return found;
    }


    public boolean hasTooLongName(ItemMeta meta){
        if(meta == null) return false;
        if(!meta.hasDisplayName()) return false;
        return ChatColor.stripColor(meta.getDisplayName()).length() > limit.get();
    }

    public ItemMeta trimName(ItemMeta meta){
        meta.setDisplayName(meta.getDisplayName().substring(0, Math.min(meta.getDisplayName().length(), limit.get())));
        return meta;
    }
}
