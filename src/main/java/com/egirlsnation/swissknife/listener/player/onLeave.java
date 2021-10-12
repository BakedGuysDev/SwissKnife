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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.CombatCheck;
import com.egirlsnation.swissknife.util.cooldownManager.CooldownManager;
import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.egirlsnation.swissknife.SwissKnife.Config.enableShitlist;
import static com.egirlsnation.swissknife.SwissKnife.Config.leakCoords;
import static com.egirlsnation.swissknife.listener.player.onSwapHandItems.handSwapDelay;

public class onLeave implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();
    private final CombatCheck combatCheck = new CombatCheck();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    private final SwissKnife plugin;
    public onLeave(SwissKnife plugin){ this.plugin = plugin; }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e){

        if(plugin.SQL.isConnected()){
            if(e.getReason().equals(PlayerQuitEvent.QuitReason.DISCONNECTED) && combatCheck.isInCombat(e.getPlayer())){
                plugin.sqlQuery.increaseCombatLog(e.getPlayer().getUniqueId());
            }
            plugin.sqlQuery.updateValues(e.getPlayer());

            if(leakCoords && enableShitlist && plugin.sqlQuery.isShitlisted(e.getPlayer())){
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The coords of " + e.getPlayer().getDisplayName() + ChatColor.GREEN + " are " + getFormattedCoords(e.getPlayer().getLocation())),40);
            }
        }

        cooldownManager.removePlayer(e.getPlayer());
        handSwapDelay.remove(e.getPlayer());

        if(combatCheck.getElytraMap().containsKey(e.getPlayer().getUniqueId())){
            combatCheck.getElytraMap().get(e.getPlayer().getUniqueId()).cancel();
            combatCheck.getElytraMap().remove(e.getPlayer().getUniqueId());
        }

        combatCheck.getElytraMap().remove(e.getPlayer().getUniqueId());
        customItemHandler.getCrystalEnabledList().remove(e.getPlayer().getUniqueId());
        customItemHandler.getDisabledPlayersList().remove(e.getPlayer().getUniqueId());
        combatCheck.removePlayer(e.getPlayer());

    }

    private String getFormattedCoords(Location loc){
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();

        return "X: " + x + " Y: " + y + " Z: " + z;
    }
}
