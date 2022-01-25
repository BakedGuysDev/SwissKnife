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

package com.egirlsnation.swissknife.systems;

import java.util.HashMap;
import java.util.Map;

public class Systems {
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends System>, System<?>> systems = new HashMap<>();

    public static void init(){

    }

    private static System<?> add(System<?> system){
        systems.put(system.getClass(), system);
        system.init();

        return system;
    }

    @SuppressWarnings("unchecked")
    public static <T extends System<?>> T get(Class<T> klass){
        return (T) systems.get(klass);
    }
}
