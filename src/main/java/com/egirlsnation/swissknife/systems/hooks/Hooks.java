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
import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import com.egirlsnation.swissknife.systems.hooks.votingPlugin.VotingPluginHook;
import org.bukkit.Bukkit;
import org.simpleyaml.configuration.ConfigurationSection;

import java.io.IOException;
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
        add(new VotingPluginHook());
    }

    @Override
    public void writeToConfig() {
        for(Hook hook : hooks){
            ConfigurationSection section = getFile().createSection(hook.name);
            section.set("enabled", hook.isEnabled());
        }
        try {
            getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFromConfig() {
        for(Hook hook : hooks){
            ConfigurationSection section = getFile().createSection(hook.name);
            boolean enabled = section.getBoolean("enabled");

            if(enabled && !hook.isEnabled()){
                hook.toggleEnabled();
            }
        }
        try {
            getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortHooks(){
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
