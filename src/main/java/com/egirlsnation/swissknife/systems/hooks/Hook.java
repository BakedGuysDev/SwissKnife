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

package com.egirlsnation.swissknife.systems.hooks;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
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
}
