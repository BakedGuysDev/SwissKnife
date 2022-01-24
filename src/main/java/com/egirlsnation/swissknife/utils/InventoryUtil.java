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

package com.egirlsnation.swissknife.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    public static boolean scanAndTrimArmorStacks(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(ItemUtil.isArmorPiece(item) && item.getAmount() > Config.instance.maxArmorStack){
                item.setAmount(Config.instance.maxArmorStack);
                found = true;
            }
        }
        return found;
    }

    public static boolean scanAndTrimTotemStack(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(ItemUtil.isArmorPiece(item) && item.getAmount() > Config.instance.maxArmorStack){
                item.setAmount(Config.instance.maxArmorStack);
                found = true;
            }
        }
        return found;
    }

}
