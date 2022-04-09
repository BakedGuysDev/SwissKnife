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

package com.egirlsnation.swissknife.systems.commands;

import com.egirlsnation.swissknife.systems.System;
import com.egirlsnation.swissknife.systems.Systems;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.*;

public class Commands extends System<Commands> {

    private final List<Command> commands = new ArrayList<>();
    private final Map<Class<? extends Command>, Command> commandInstances = new HashMap<>();

    private final List<Command> enabled = new ArrayList<>();


    public Commands(){
        super("commands");
    }

    public static Commands get(){
        return Systems.get(Commands.class);
    }

    @Override
    public void init(){
        add(new KillCommand());
        add(new MonkeyCommand());
        add(new PingCommand());
        add(new PlaytimeCommand());
        add(new RefreshRankCommand());
        add(new ShitListCommand());
        add(new ShrugCommand());
        add(new SwissKnifeCommand());
        add(new ToggleItemAbilityCommand());
        add(new TpsAlertTestCommand());
    }

    public void sortCommands(){
        commands.sort(Comparator.comparing(o -> o.name));
    }

    private void add(Command command){
        commandInstances.put(command.getClass(), command);
        commands.add(command);
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T get(Class<T> klass){
        return (T) commandInstances.get(klass);
    }

    public boolean isEnabled(Class<? extends Command> klass){
        Command command = get(klass);
        return command != null && command.isEnabled();
    }

    void addEnabled(Command command){
        synchronized(enabled){
            if(!enabled.contains(command)){
                enabled.add(command);
            }
        }
    }

    void removeEnabled(Command command){
        synchronized(enabled){
            enabled.remove(command);
        }
    }

    @Override
    public void writeToConfig(){
        for(Command command : commands){
            ConfigurationSection section = getFile().createSection(command.name);
            section.set("enabled", false);
            section.set("cooldown", 0);
        }

        try{
            getFile().save();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readFromConfig(){
        for(Command command : commands){
            ConfigurationSection section = getFile().getConfigurationSection(command.name);
            if(section == null){
                section = getFile().createSection(command.name);
                section.set("enabled", false);
                section.set("cooldown", 0);
                continue;
            }
            boolean enabled = section.getBoolean("enabled");
            command.setCooldown(section.getInt("cooldown"));

            if(enabled && !command.isEnabled()){
                command.toggle();
            }
            if(!enabled && command.isEnabled()){
                command.toggle();
            }
            command.register();
        }
        try{
            getFile().save();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<String> playerNamesTabComplete(@NotNull CommandSender sender, String[] args, int position){
        if(args == null) return null;
        if(args.length > position) return null;
        final List<String> completions = new ArrayList<>(1);
        final List<String> playerNames = new ArrayList<>(1);
        for(Player p : Bukkit.getOnlinePlayers()){
            playerNames.add(p.getName());
        }
        StringUtil.copyPartialMatches(args[position - 1], playerNames, completions);
        return completions;
    }
}
