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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.egirlsnation.swissknife.SwissKnife.Config.crystalsPerSecond;
import static com.egirlsnation.swissknife.SwissKnife.Config.limitCrystalPlacementSpeed;

public class onPlayerPlaceCrystal implements Listener {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    private final static Map<UUID, Long> crystalMap = new HashMap<>();

    @EventHandler
    private void PlayerPlaceCrystal(PlayerPlaceCrystalEvent e){
        if(limitCrystalPlacementSpeed){
            UUID uuid = e.getPlayer().getUniqueId();
            if(!crystalMap.containsKey(uuid)){
                crystalMap.put(uuid, System.currentTimeMillis());
            }else{
                long timeLeft = System.currentTimeMillis() - crystalMap.get(uuid);
                if(timeLeft < ((double)1000 / (double)crystalsPerSecond)){
                    e.setCancelled(true);
                }else{
                    crystalMap.put(uuid, System.currentTimeMillis());
                }
            }
        }


        if(customItemHandler.isDraconiteCrystal(e.getCrystalItem())) {
            e.getCrystal().setCustomName("Draconite Crystal");
        }
    }
}
