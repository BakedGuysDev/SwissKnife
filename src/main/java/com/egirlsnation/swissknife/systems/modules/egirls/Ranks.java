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

package com.egirlsnation.swissknife.systems.modules.egirls;

import com.egirlsnation.swissknife.systems.hooks.Hooks;
import com.egirlsnation.swissknife.systems.hooks.votingPlugin.VotingPluginHook;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.player.RankUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Ranks extends Module {
    public Ranks() {
        super(Categories.EgirlsNation, "ranks", "Egirls Nation rank system");
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        if(!isEnabled()) return;
        if(!e.getPlayer().hasPlayedBefore()) return;
        Hooks.get().get(VotingPluginHook.class).getVotes(e.getPlayer());
        RankUtil.promoteIfEligible(e.getPlayer());
    }
}
