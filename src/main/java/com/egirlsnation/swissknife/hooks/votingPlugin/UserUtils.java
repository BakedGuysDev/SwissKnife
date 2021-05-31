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
}
