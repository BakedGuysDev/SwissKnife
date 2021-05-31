package com.egirlsnation.swissknife.hooks;

import com.bencodez.votingplugin.user.UserManager;
import com.egirlsnation.swissknife.hooks.votingPlugin.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginManagerEvents implements Listener {

    private final UserUtils userUtils = new UserUtils();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEnable(PluginEnableEvent e){
        if("VotingPlugin".equals(e.getPlugin().getName())){
            if(userUtils.getUserManager() != null) return;
            Bukkit.getLogger().info("Enabling VotingPlugin hook.");
            userUtils.setUserManager(UserManager.getInstance());
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEnable(PluginDisableEvent e){
        if("VotingPlugin".equals(e.getPlugin().getName())){
            if(userUtils.getUserManager() == null) return;
            Bukkit.getLogger().info("Disabling VotingPlugin hook.");
            userUtils.setUserManager(null);
        }
    }
}
