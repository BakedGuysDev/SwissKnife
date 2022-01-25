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

import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//TODO: Hook system
public abstract class Hook implements Listener, Comparable<Hook> {

    public final String name;
    public final String title;
    public final String pluginName;

    private boolean enabled;
    private boolean active;

    public Hook(String name, String pluginName){
        this.name = name;
        this.pluginName = pluginName;
        this.title = StringUtil.nameToTitle(name);
    }

    public void onEnable() {}
    public void onDisable() {}

    public void onActivate() {}
    public void onDeactivate() {}

    public void toggle(){
        if(!enabled){
            enabled = true;
            Hooks.get().addEnabled(this);
            onActivate();

        }else{
            enabled = false;
            Hooks.get().removeEnabled(this);
            onDeactivate();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
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
}
