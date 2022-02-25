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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.StringUtil;
import com.egirlsnation.swissknife.utils.handlers.CombatCheckHandler;
import com.egirlsnation.swissknife.utils.handlers.commandCooldown.CooldownHandler;
import com.egirlsnation.swissknife.utils.handlers.customItems.DraconiteAbilityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onLeave implements Listener {

    private final CooldownHandler cooldownHandler = new CooldownHandler();
    private final CombatCheckHandler combatCheckHandler = new CombatCheckHandler();
    private final DraconiteAbilityHandler draconiteAbilityHandler = new DraconiteAbilityHandler();
    private final StringUtil stringUtils = new StringUtil();

    private final SwissKnife plugin;
    public onLeave(SwissKnife plugin){ this.plugin = plugin; }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e){
        /*
        if(plugin.SQL.isConnected()){
            if(e.getReason().equals(PlayerQuitEvent.QuitReason.DISCONNECTED) && combatCheckHandler.isInCombat(e.getPlayer())){
                plugin.playerStatsDriver.increaseCombatLog(e.getPlayer().getUniqueId());
            }
            plugin.playerStatsDriver.updateValuesAsync(e.getPlayer());

            if(OldConfig.instance.leakCoords && OldConfig.instance.enableShitlist && plugin.playerStatsDriver.isShitlisted(e.getPlayer())){
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
        combatCheckHandler.removePlayer(e.getPlayer());
         */
    }


}
