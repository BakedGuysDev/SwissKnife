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

package com.egirlsnation.swissknife.systems.hooks;

import com.bencodez.votingplugin.user.UserManager;
import org.bukkit.entity.Player;

public class VotingPluginHook extends Hook {

    public VotingPluginHook() {
        super("voting-plugin-hook", "VotingPlugin");
    }

    private static UserManager userManager = null;

    public int getVotes(Player player){
        if(!isActive()) return 0;
        return userManager.getVotingPluginUser(player).getPoints();
    }

    @Override
    protected void initHook(){
        userManager = UserManager.getInstance();
    }

    @Override
    protected void removeHook(){
        userManager = null;
    }


}
