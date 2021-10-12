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

package com.egirlsnation.swissknife.listener.player;

import com.egirlsnation.swissknife.event.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onPlayerPlaceCrystal implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @EventHandler
    private void PlayerPlaceCrystal(PlayerPlaceCrystalEvent e){
        if(customItemHandler.isDraconiteCrystal(e.getCrystalItem())) {
            e.getCrystal().setCustomName("Draconite Crystal");
        }
    }
}
