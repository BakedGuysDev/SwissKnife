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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Hook implements Listener, Comparable<Hook> {

    public final String name;
    public final String title;
    public final String pluginName;

    private boolean enabled = false;
    private boolean active = false;

    public Hook(String name, String pluginName){
        this.name = name;
        this.pluginName = pluginName;
        this.title = StringUtil.nameToTitle(name);
    }

    public void onEnable() {}
    public void onDisable() {}

    public void onActivate() {}
    public void onDeactivate() {}

    public void init(){
        if(Bukkit.getPluginManager().getPlugin(pluginName) == null) return;

        if(Bukkit.getPluginManager().getPlugin(pluginName).isEnabled()){
            if(active) return;
            initHook();
            SwissKnife.swissLogger.info(pluginName + "hook activated.");
        }
    }

    public void toggleEnabled(){
        if(!enabled){
            enabled = true;
            Hooks.get().addEnabled(this);
            onEnable();

        }else{
            enabled = false;
            Hooks.get().removeEnabled(this);
            onDisable();
        }
    }

    public void toggleActive(){
        if(!active){
            active = true;
            Hooks.get().addActive(this);
            init();
            onActivate();
        }else{
            active = false;
            Hooks.get().removeActive(this);
            onDeactivate();
            removeHook();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isActive(){
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hook hook = (Hook) o;
        return Objects.equals(name, hook.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull Hook o) {
        return name.compareTo(o.name);
    }

    protected abstract void initHook();

    protected abstract void removeHook();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onEnable(PluginEnableEvent e){
        if(!isEnabled()) return;

        if(e.getPlugin().getName().equals(pluginName)){
            if(isActive()) return;
            SwissKnife.swissLogger.info("Activating " + pluginName + " hook.");
            toggleActive();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onDisable(PluginDisableEvent e){
        if(!isEnabled()) return;

        if(e.getPlugin().getName().equals(pluginName)){
            if(!isActive()) return;
            SwissKnife.swissLogger.info("Deactivating " + pluginName + " hook.");
            toggleActive();
        }
    }
}
