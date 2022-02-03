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

package com.egirlsnation.swissknife.listeners.player;

import com.egirlsnation.swissknife.events.PlayerPlaceCrystalEvent;
import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
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
