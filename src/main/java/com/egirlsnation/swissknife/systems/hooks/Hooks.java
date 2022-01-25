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
import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import org.bukkit.Bukkit;

import java.util.*;

public class Hooks extends System<Hook> {
    private final List<Hook> hooks = new ArrayList<>();
    private final Map<Class<? extends Hook>, Hook> hookInstances = new HashMap<>();

    private final List<Hook> enabled = new ArrayList<>();
    private final List<Hook> active = new ArrayList<>();


    public Hooks(){
        super("hooks");
    }

    public static Hooks get(){
        return Systems.get(Hooks.class);
    }

    @Override
    public void init(){

    }

    public void sortModules(){
        hooks.sort(Comparator.comparing(o -> o.title));
    }

    private void add(Hook hook){
        hookInstances.put(hook.getClass(), hook);
        hooks.add(hook);
        Bukkit.getPluginManager().registerEvents(hook, SwissKnife.getPlugin(SwissKnife.class));
    }

    @SuppressWarnings("unchecked")
    public  <T extends Hook> T get(Class<T> klass){
        return (T) hookInstances.get(klass);
    }

    public boolean isActive(Class<? extends Hook> klass) {
        Hook hook = get(klass);
        return hook != null && hook.isEnabled();
    }

    void addEnabled(Hook hook){
        synchronized (enabled){
            if(!enabled.contains(hook)){
                enabled.add(hook);
            }
        }
    }

    void removeEnabled(Hook hook){
        synchronized (enabled){
            enabled.remove(hook);
        }
    }

    void addActive(Hook hook){
        synchronized (active){
            active.add(hook);
        }
    }

    void removeActive(Hook hook){
        synchronized (active){
            active.remove(hook);
        }
    }
}
