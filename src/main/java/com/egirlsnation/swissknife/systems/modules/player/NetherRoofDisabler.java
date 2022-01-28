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

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.OldConfig;
import com.egirlsnation.swissknife.utils.SwissLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class NetherRoofDisabler extends Module {
    public NetherRoofDisabler() {
        super(Categories.Player, "nether-roof-disabler", "Prevents players from going onto the nether roof");
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e) {
        if(!isEnabled()) return;
        if (OldConfig.instance.preventPlayersOnNether) {
            Location l = e.getTo();
            if (!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if (l.getY() >= OldConfig.instance.netherRoofHeight) {
                SwissLogger.info("Player " + e.getPlayer().getName() + " attempted to go above the nether roof");
                e.setCancelled(true);
                if (OldConfig.instance.teleportPlayersDown) {
                    e.getPlayer().teleport(e.getPlayer().getLocation().subtract(0, 3, 0));
                }
                if (OldConfig.instance.dmgPlayersOnNether) {
                    e.getPlayer().damage(OldConfig.instance.dmgToDealNether);
                }
            }
        }
    }

    @EventHandler
    public void PlayerTeleport(PlayerTeleportEvent e){
        if(!isEnabled()) return;
        if(OldConfig.instance.preventPlayersOnNether){
            Location l = e.getTo();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if(l.getBlockY() >= OldConfig.instance.netherRoofHeight){
                SwissLogger.info("Player " + e.getPlayer().getName() + " attempted to go above the nether roof");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void VehicleEnter(VehicleEnterEvent e){
        if(OldConfig.instance.preventPlayersOnNether && e.getEntered() instanceof Player){
            Location l = e.getVehicle().getLocation();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if(l.getBlockY() >= OldConfig.instance.netherRoofHeight && !e.getEntered().isOp()){
                e.setCancelled(true);
                SwissLogger.info("Player " + e.getEntered().getName() + " attempted to go above the nether roof");
                e.getVehicle().remove();
            }
        }
    }

    @EventHandler
    public void VehicleExit(VehicleExitEvent e){
        if(OldConfig.instance.preventPlayersOnNether && e.getExited() instanceof Player){
            Location l = e.getVehicle().getLocation();
            if(!l.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
            if(l.getBlockY() >= OldConfig.instance.netherRoofHeight && !e.getExited().isOp()){
                e.setCancelled(true);
                SwissLogger.info("Player " + e.getExited().getName() + " attempted to go above the nether roof");
                e.getVehicle().remove();
            }
        }
    }
}
