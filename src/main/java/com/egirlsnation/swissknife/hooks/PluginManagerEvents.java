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

package com.egirlsnation.swissknife.hooks;

import com.egirlsnation.swissknife.hooks.votingPlugin.VotingPluginHook;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginManagerEvents implements Listener {

    private final VotingPluginHook votingPluginHook = new VotingPluginHook();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEnable(PluginEnableEvent e){
        if("VotingPlugin".equals(e.getPlugin().getName())){
            if(votingPluginHook.isVotingPluginHookActive()) return;
            Bukkit.getLogger().info("Enabling VotingPlugin hook.");
            votingPluginHook.initVotingPluginHook();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDisable(PluginDisableEvent e){
        if("VotingPlugin".equals(e.getPlugin().getName())){
            if(!votingPluginHook.isVotingPluginHookActive()) return;
            Bukkit.getLogger().info("Disabling VotingPlugin hook.");
            votingPluginHook.removeVotingPluginHook();
        }
    }
}
