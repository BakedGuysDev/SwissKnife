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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.handlers.CombatCheckHandler;
import com.egirlsnation.swissknife.systems.handlers.commandCooldown.CooldownHandler;
import com.egirlsnation.swissknife.systems.handlers.customItems.CustomItemHandler;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.egirlsnation.swissknife.listeners.player.onSwapHandItems.handSwapDelay;

public class onLeave implements Listener {

    private final CooldownHandler cooldownHandler = new CooldownHandler();
    private final CombatCheckHandler combatCheckHandler = new CombatCheckHandler();
    private final CustomItemHandler customItemHandler = new CustomItemHandler();
    private final StringUtil stringUtils = new StringUtil();

    private final SwissKnife plugin;
    public onLeave(SwissKnife plugin){ this.plugin = plugin; }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e){

        if(plugin.SQL.isConnected()){
            if(e.getReason().equals(PlayerQuitEvent.QuitReason.DISCONNECTED) && combatCheckHandler.isInCombat(e.getPlayer())){
                plugin.sqlQuery.increaseCombatLog(e.getPlayer().getUniqueId());
            }
            plugin.sqlQuery.updateValues(e.getPlayer());

            if(Config.instance.leakCoords && Config.instance.enableShitlist && plugin.sqlQuery.isShitlisted(e.getPlayer())){
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The coords of " + e.getPlayer().getDisplayName() + ChatColor.GREEN + " are " + stringUtils.getFormattedCoords(e.getPlayer().getLocation())),40);
            }
        }

        cooldownHandler.removePlayer(e.getPlayer());
        handSwapDelay.remove(e.getPlayer());

        if(combatCheckHandler.getElytraMap().containsKey(e.getPlayer().getUniqueId())){
            combatCheckHandler.getElytraMap().get(e.getPlayer().getUniqueId()).cancel();
            combatCheckHandler.getElytraMap().remove(e.getPlayer().getUniqueId());
        }

        combatCheckHandler.getElytraMap().remove(e.getPlayer().getUniqueId());
        customItemHandler.getCrystalEnabledList().remove(e.getPlayer().getUniqueId());
        customItemHandler.getDisabledPlayersList().remove(e.getPlayer().getUniqueId());
        combatCheckHandler.removePlayer(e.getPlayer());

    }


}
