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

package com.egirlsnation.swissknife.listeners.player;

import com.egirlsnation.swissknife.systems.handlers.RespawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.mainWorldName;

public class onRespawn implements Listener {

    private final RespawnHandler respawnHandler = new RespawnHandler();

    @EventHandler
    private void Respawn(PlayerRespawnEvent e){
        if(e.getPlayer().getBedSpawnLocation() != null) return;
        e.setRespawnLocation(respawnHandler.getRandomLocation(Bukkit.getWorld(mainWorldName)));
    }
}
