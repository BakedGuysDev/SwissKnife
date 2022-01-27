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

package com.egirlsnation.swissknife.systems.hooks.votingPlugin;

import com.egirlsnation.swissknife.systems.hooks.Hook;
import com.egirlsnation.swissknife.utils.SwissLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class VotingPluginHook extends Hook {

    public VotingPluginHook() {
        super("voting-plugin-hook", "VotingPlugin");
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onEnable(PluginEnableEvent e){
        if(!isEnabled()) return;

        if(e.getPlugin().getName().equals(pluginName)){
            if(isActive()) return;
            SwissLogger.info("Enabling VotingPlugin hook.");
            initHook();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onDisable(PluginDisableEvent e){
        if(!isEnabled()) return;

        if(e.getPlugin().getName().equals(pluginName)){
            if(!isActive()) return;
            SwissLogger.info("Disabling VotingPlugin hook.");
            removeHook();
        }
    }

    @Override
    protected void initHook(){
        VpUserManager.initUserManager();
        setActive(true);
    }

    @Override
    protected void removeHook(){
        VpUserManager.removeUserManager();
        setActive(false);
    }


}
