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
    private static final List<Module> active = new ArrayList<>();

    public static void init(){

    }

    private static void add(Module module){
        moduleInstances.put(module.getClass(), module);
        modules.add(module);
        Bukkit.getPluginManager().registerEvents(module, SwissKnife.getPlugin(SwissKnife.class));
    }

    @SuppressWarnings("unchecked")
    public static  <T extends Module> T get(Class<T> klass){
        return (T) moduleInstances.get(klass);
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = get(klass);
        return module != null && module.isActive();
    }

    static void addActive(Module module){
        synchronized (active){
            if(!active.contains(module)){
                active.add(module);
            }
        }
    }

    static void removeActive(Module module){
        synchronized (active){
            active.remove(module);
        }
    }
}
