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

import com.bencodez.votingplugin.user.UserManager;
import com.bencodez.votingplugin.user.VotingPluginUser;
import org.bukkit.entity.Player;

public class UserUtils {

    private static UserManager userManager = null;

    public void setUserManager(){
        userManager = UserManager.getInstance();
    }

    public void removeUserManager(){
        userManager = null;
    }

    public UserManager getUserManager() {
        return userManager;
    }


    public double getVotes(Player player){
        VotingPluginUser votingUser = userManager.getVotingPluginUser(player);
        return votingUser.getPoints();
    }
}
