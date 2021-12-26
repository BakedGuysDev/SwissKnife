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

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modules {
    private static final List<Module> modules = new ArrayList<>();
    private static final Map<Class<? extends Module>, Module> moduleInstances = new HashMap<>();

    public Module get(Class<? extends Module> klass){
        return moduleInstances.get(klass);
    }

    private void init(){

    }

    public void add(Module module){
        moduleInstances.put(module.getClass(), module);
        modules.add(module);
        Bukkit.getPluginManager().registerEvents(module, SwissKnife.getPlugin(SwissKnife.class));
    }
}
