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

package com.egirlsnation.swissknife.utils.entity.player;

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GamemodeUtil {

    public static void ensureFlyDisable(Player player){
        if(!player.hasPermission("swissknife.bypass.fly")){
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    public static void removeClickedItem(Player player){
        if(player.hasPermission("swissknife.bypass.creative")) return;
        if(player.getItemOnCursor().getType().equals(Material.AIR)) return;
        ItemStack clickedItem = player.getItemOnCursor();
        if(clickedItem.getItemMeta() == null){
            SwissKnife.swissLogger.info(player.getCustomName() + " tried to bring a " + clickedItem.getType() + " out of creative. (Possibly illegal?)" );
        }else{
            SwissKnife.swissLogger.info(player.getCustomName() + " tried to bring a " + clickedItem.getItemMeta().displayName() + "( " + clickedItem.getType() + ") out of creative. (Possibly illegal?)" );
        }
        player.setItemOnCursor(null);
    }
}
