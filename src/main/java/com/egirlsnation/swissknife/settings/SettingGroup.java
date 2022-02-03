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

import com.egirlsnation.swissknife.systems.modules.Module;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SettingGroup implements Iterable<Setting<Object>> {
    public final String name;
    final List<Setting<Object>> settings = new ArrayList<>(1);

    SettingGroup(String name){
        this.name = name;
    }

    public Setting<Object> get(String name){
        for(Setting<Object> setting : this) {
            if(setting.name.equalsIgnoreCase(name)) return setting;
        }

        return null;
    }

    public <T> Setting<T> add(Setting<T> setting){
        settings.add((Setting<Object>) setting);
        return setting;
    }

    public void writeToConfig(YamlFile file, Module module){
        ConfigurationSection section = file.createSection( module.category.name + "." + module.name + "." + name);
        for (Setting<Object> setting : settings) {
            setting.writeToConfig(file, module, this, section);
        }
    }


    @Override
    public Iterator<Setting<Object>> iterator() {
        return settings.iterator();
    }
}
