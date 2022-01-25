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

package com.egirlsnation.swissknife.systems.modules;

import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Module implements Listener, Comparable<Module> {

    public final Category category;
    public final String name;
    public final String title;
    public final String description;

    private boolean enabled;

    public Module(Category category ,String name, String description){
        this.category = category;
        this.name = name;
        this.title = StringUtil.nameToTitle(name);
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onActivate() {}
    public void onDeactivate() {}

    public void toggle(){
        if(!enabled){
            enabled = true;
            Modules.get().addEnabled(this);
            onActivate();

        }else{
            enabled = false;
            Modules.get().removeEnabled(this);
            onDeactivate();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(name, module.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull Module o) {
        return name.compareTo(o.name);
    }
}
