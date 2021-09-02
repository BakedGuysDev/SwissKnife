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

package com.egirlsnation.swissknife.hooks.votingPlugin;

import com.bencodez.votingplugin.VotingPluginHooks;
import com.bencodez.votingplugin.user.UserManager;
import com.bencodez.votingplugin.user.VotingPluginUser;
import org.bukkit.entity.Player;

public class UserUtils {

    private VotingPluginHooks votingPluginHooks;
    private UserManager userManager;

    public VotingPluginHooks getVotingPluginHooks() {
        return votingPluginHooks;
    }

    public void setVotingPluginHooks(VotingPluginHooks votingPluginHooks) {
        this.votingPluginHooks = votingPluginHooks;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public double getVotes(Player player){
        VotingPluginUser votingUser = userManager.getVotingPluginUser(player);
        return votingUser.getPoints();
    }

    public boolean isVotingPluginHookActive(){
        return votingPluginHooks != null;
    }
}
