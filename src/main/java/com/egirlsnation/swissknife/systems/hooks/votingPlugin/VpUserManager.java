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

package com.egirlsnation.swissknife.systems.hooks.votingPlugin;

import com.bencodez.votingplugin.user.UserManager;
import com.egirlsnation.swissknife.systems.hooks.Hooks;
import org.bukkit.entity.Player;

public class VpUserManager {
    private static UserManager userManager = null;

    public static void initUserManager(){
        userManager = UserManager.getInstance();
    }

    public static void removeUserManager(){
        userManager = null;
    }

    public static double getVotes(Player player){
        if(!Hooks.get().isActive(VotingPluginHook.class)) return 0;
        return userManager.getVotingPluginUser(player).getPoints();
    }
}
