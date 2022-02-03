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

package com.egirlsnation.swissknife.systems;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.hooks.Hooks;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.utils.SwissLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Systems {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends System>, System<?>> systems = new HashMap<>();
    private static final List<Runnable> preLoadTasks = new ArrayList<>(1);

    public static void addPreLoadTask(Runnable task){
        preLoadTasks.add(task);
    }

    public static void init(){
        add(new Modules());
        add(new Hooks());
    }

    private static System<?> add(System<?> system){
        systems.put(system.getClass(), system);
        system.init();

        return system;
    }

    public static void save(){
        long start = java.lang.System.currentTimeMillis();
        SwissKnife.swissLogger.info("Saving configurations...");

        for(System<?> system : systems.values()) system.save();

        long end = java.lang.System.currentTimeMillis() - start;
        SwissKnife.swissLogger.info("Saved in " +  end + " milliseconds.");
    }

    public static void load(){
        long start = java.lang.System.currentTimeMillis();
        SwissKnife.swissLogger.info("Loading configurations...");

        for(Runnable task : preLoadTasks) task.run();
        for(System<?> system : systems.values()) system.load();
        long end = java.lang.System.currentTimeMillis() - start;
        SwissKnife.swissLogger.info("Loaded in " +  end + " milliseconds.");
    }


    @SuppressWarnings("unchecked")
    public static <T extends System<?>> T get(Class<T> klass){
        return (T) systems.get(klass);
    }
}
