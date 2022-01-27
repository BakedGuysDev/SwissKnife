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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.utils.SwissLogger;
import org.apache.commons.lang.StringUtils;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;

public abstract class System<T>{
    private final String name;
    private YamlFile file;

    public System(String name){
        this.name = name;

        if(name != null){
            this.file = new YamlFile(SwissKnife.INSTANCE.getDataFolder() + "/" + name + ".yml");
        }
    }

    public void init(){}

    public void save(){
        YamlFile file  = getFile();
        if(file == null) return;

        try{
            writeToConfig();
            file.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        YamlFile file = getFile();
        if(file == null) return;

        try{
            if(file.exists()){
                file.loadWithComments();
            }else{
                SwissLogger.info(StringUtils.capitalize(getName()) +" config doesn't exist. Creating default one.");
                writeToConfig();
            }
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

    }


    public YamlFile getFile(){
        return file;
    }

    public String getName(){
        return name;
    }

    public abstract void writeToConfig();

    public abstract void readFromConfig();
}
