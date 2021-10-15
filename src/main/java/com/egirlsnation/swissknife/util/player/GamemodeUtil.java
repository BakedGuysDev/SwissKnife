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

package com.egirlsnation.swissknife.util.player;

import com.egirlsnation.swissknife.util.LOGGER;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GamemodeUtil {

    public void ensureFlyDisable(Player player){
        if(!player.hasPermission("swissknife.bypass.fly")){
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    public void removeClickedItem(Player player){
        if(player.hasPermission("swissknife.bypass.creative")) return;
        if(player.getItemOnCursor() == null) return;
        ItemStack clickedItem = player.getItemOnCursor();
        if(clickedItem.getItemMeta() == null){
            LOGGER.info(player.getCustomName() + " tried to bring a " + clickedItem.getType() + " out of creative. (Possibly illegal?)" );
        }else{
            LOGGER.info(player.getCustomName() + " tried to bring a " + clickedItem.getItemMeta().getDisplayName() + "( " + clickedItem.getType() + ") out of creative. (Possibly illegal?)" );
        }
        player.setItemOnCursor(null);
    }
}
