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

    public boolean useCommandPrefix = true;
    public String commandPrefix = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "SwissKnife | %command%" + ChatColor.GOLD + "]" + ChatColor.RESET;

    public boolean disableMetrics = false;

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

        section.set("use-command-prefix", useCommandPrefix);
        file.setComment("config.use-prefix", "If plugin should use command prefix when sending messages to players from commands");
        section.set("command-prefix", commandPrefix);

        section.set("disable-metrics", disableMetrics);
        file.setComment("config.disable-metrics", "SwissKnife uses bStats to collect basic anonymous stats about the servers running the plugin. Use this option to opt-out.");

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

        if(section.get("prefix") == null){
            section.set("prefix", prefix);
        }else{
            prefix = section.getString("prefix");
        }

        if(section.get("use-module-prefix") == null){
            section.set("use-module-prefix", useModulePrefix);
        }else{
            useModulePrefix = section.getBoolean("use-module-prefix");
        }

        if(section.get("module-prefix") == null){
            section.set("module-prefix", modulePrefix);
        }else{
            modulePrefix = section.getString("module-prefix");
        }

        if(section.get("use-command-prefix") == null){
            section.set("use-command-prefix", useCommandPrefix);
        }else{
            useCommandPrefix = section.getBoolean("use-command-prefix");
        }

        if(section.get("command-prefix") == null){
            section.set("command-prefix", commandPrefix);
        }else{
            commandPrefix = section.getString("command-prefix");
        }

        if(section.get("use-module-prefix") == null){
            section.set("use-module-prefix", useModulePrefix);
        }else{
            useModulePrefix = section.getBoolean("use-module-prefix");
        }

        if(section.get("disable-metrics") == null){
            section.set("disable-metrics", disableMetrics);
        }else{
            disableMetrics = section.getBoolean("disable-metrics");
        }
    }
}
