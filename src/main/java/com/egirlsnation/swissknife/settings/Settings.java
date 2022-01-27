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

package com.egirlsnation.swissknife.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Settings implements Iterable<Setting<Object>> {
    final List<Setting<Object>> settings = new ArrayList<>(1);

    public Setting<Object> get(String name){
        for(Setting<Object> setting : this) {
            if(setting.name.equalsIgnoreCase(name)) return setting;
        }

        return null;
    }

    public Setting<Object> add(Setting<Object> setting){
        settings.add(setting);
        return setting;
    }


    @Override
    public Iterator<Setting<Object>> iterator() {
        return settings.iterator();
    }
}
