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

package com.egirlsnation.swissknife.systems.modules;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.settings.Settings;
import com.egirlsnation.swissknife.systems.config.Config;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.Objects;
import java.util.logging.Level;

public abstract class Module implements Listener, Comparable<Module> {

    public final Category category;
    public final String name;
    public final String title;
    public final String description;

    public final Settings settings = new Settings();

    private boolean enabled = false;

    public Module(Category category ,String name, String description){
        this.category = category;
        this.name = name;
        this.title = StringUtil.nameToTitle(name);
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onEnable() {}
    public void onDisable() {}

    public void toggle(){
        if(!enabled){
            enabled = true;
            Modules.get().addEnabled(this);
            onEnable();

        }else{
            enabled = false;
            Modules.get().removeEnabled(this);
            onDisable();
        }
    }

    public void info(String message){
        SwissKnife.swissLogger.log(Level.INFO, ChatColor.AQUA + message, "SwissKnife|" + name);
    }

    public void warn(String message){
        SwissKnife.swissLogger.log(Level.WARNING, message, "SwissKnife|" + name);
    }

    public void error(String message){
        SwissKnife.swissLogger.log(Level.SEVERE, message, "SwissKnife|" + name);
    }

    public void sendMessage(Player player, String message){
        if(Config.get().useModulePrefix){
            player.sendMessage(Config.get().modulePrefix.replaceAll("%module%", name) + " " + message);
        }else{
            player.sendMessage(message);
        }
    }

    public void sendMessage(Player player, String message, boolean mask){
        if(Config.get().useModulePrefix){
            if(mask){
                player.sendMessage(Config.get().modulePrefix.replaceAll("%module%", "Modules") + " " + message);
            }else{
                player.sendMessage(Config.get().modulePrefix.replaceAll("%module%", name) + " " + message);
            }
        }else{
            player.sendMessage(message);
        }
    }

    public void writeToConfig(YamlFile file){
        ConfigurationSection section = file.createSection(category.name + "." + name);

        file.setComment(category.name + "." + name, description, CommentType.SIDE);
        section.set("enabled", isEnabled());

        for (SettingGroup sg : settings) {
            sg.writeToConfig(file, this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(name, module.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull Module o) {
        return name.compareTo(o.name);
    }
}
