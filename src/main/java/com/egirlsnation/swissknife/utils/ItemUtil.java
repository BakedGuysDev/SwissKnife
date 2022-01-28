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

package com.egirlsnation.swissknife.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {

    public static boolean isArmorPiece(@Nullable ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType().name();
        return itemTypeString.endsWith("_HELMET")
                || itemTypeString.endsWith("_CHESTPLATE")
                || itemTypeString.endsWith("_LEGGINGS")
                || itemTypeString.endsWith("_BOOTS");
    }

    public boolean isSpawnEgg(@Nullable ItemStack item){
        if(item == null) return false;
        return item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG");
    }

    public boolean hasTooLongName(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasDisplayName()) return false;
        return meta.getDisplayName().length() > OldConfig.instance.maxItemNameLength;
    }

    public ItemMeta trimName(@NotNull ItemStack item){
        if(item.getItemMeta() == null) return null;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().substring(0, Math.min(meta.getDisplayName().length(), OldConfig.instance.maxItemNameLength)));
        return meta;
    }

    public static boolean isAncientOrDraconite(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        if(!item.getItemMeta().hasLore()) return false;
        if(item.getItemMeta().getLore() == null) return false;
        if(item.getItemMeta().getLore().contains("§6Ancient weapon")){
            return true;
        }
        if(item.getItemMeta().getLore().contains("§cDraconite Weapon")){
            return true;
        }
        return false;
    }

}
