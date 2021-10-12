/*
 *
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 *
 */

package com.egirlsnation.swissknife.listener.player;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.netherRoofHeight;
import static com.egirlsnation.swissknife.SwissKnife.Config.preventPlayersOnNether;

public class onPlayerTeleport implements Listener {

    @EventHandler
    public void PlayerTeleport(PlayerTeleportEvent e){
        if(preventPlayersOnNether){
            Location l = e.getTo();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if(l.getBlockY() >= netherRoofHeight){
                e.setCancelled(true);
            }
        }
    }
}
