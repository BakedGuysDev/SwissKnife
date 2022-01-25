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

public abstract class Module implements Listener {

    public final Category category;
    public final String name;
    public final String title;
    public final String description;

    private boolean active;

    public Module(Category category ,String name, String description){
        this.category = category;
        this.name = name;
        this.title = StringUtil.nameToTitle(name);
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void toggle(){
        if(!active){
            active = true;
            Modules.get().addActive(this);
        }
    }
}
