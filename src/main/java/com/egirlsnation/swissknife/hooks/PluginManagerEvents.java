package com.egirlsnation.swissknife.hooks;

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.logging.Logger;

public class PluginManagerEvents implements Listener {

    private final SwissKnife swissKnife;

    public PluginManagerEvents(SwissKnife swissKnife){ this.swissKnife = swissKnife; }

    private final Logger LOGGER = Bukkit.getLogger();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEnable(PluginEnableEvent e){
        if("AdvancedTeleport".equals(e.getPlugin().getName())){

        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEnable(PluginDisableEvent e){
        if("AdvancedTeleport".equals(e.getPlugin().getName())){

        }
    }
}
