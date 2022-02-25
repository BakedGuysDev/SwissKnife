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

package com.egirlsnation.swissknife.systems.config;

import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import org.bukkit.ChatColor;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class Config extends System<Config> {
    public String prefix = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "SwissKnife" + ChatColor.GOLD + "]" + ChatColor.RESET;

    public boolean useModulePrefix = true;
    public String modulePrefix = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "SwissKnife | %module%" + ChatColor.GOLD + "]" + ChatColor.RESET;

    public Config(){
        super("config");
    }

    public static Config get(){
        return Systems.get(Config.class);
    }

    @Override
    public void writeToConfig(){
        YamlFile file = getFile();
        ConfigurationSection section = file.createSection("config");

        file.setComment("config", "\nGeneral config options\n", CommentType.BLOCK);
        section.set("prefix", prefix);
        section.set("use-module-prefix", useModulePrefix);
        file.setComment("config.use-prefix", "If plugin should use module prefix when alerting players");
        section.set("module-prefix", modulePrefix);

        try {
            getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFromConfig(){
        YamlFile file = getFile();
        ConfigurationSection section = file.getConfigurationSection("config");
        if(section == null){
            writeToConfig();
            return;
        }

        prefix = section.getString("prefix");
        useModulePrefix = section.getBoolean("use-module-prefix");
        modulePrefix = section.getString("module-prefix");
    }
}
