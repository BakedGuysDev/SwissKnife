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
import org.bukkit.event.player.PlayerMoveEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class onPlayerMove implements Listener {

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        if(preventPlayersOnNether){
            Location l = e.getTo();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if(l.getY() >= netherRoofHeight){
                if(e.getFrom().getBlockY() >= netherRoofHeight){
                    e.setCancelled(true);
                    if(teleportPlayersDown){
                        e.getPlayer().teleport(e.getPlayer().getLocation().subtract(0, 3, 0));
                    }
                    if(dmgPlayersOnNether){
                        e.getPlayer().damage(dmgToDealNether);
                    }
                }
            }
        }
    }
}
