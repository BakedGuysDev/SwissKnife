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

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public abstract class Command implements CommandExecutor {
    public String name;
    private int cooldown = 0;
    private boolean enabled = false;

    private final Map<UUID, Long> cooldownMap = new HashMap<>(1);

    public Command(String name){
        this.name = name;
    }

    public void setCooldown(int cooldown){
        this.cooldown = cooldown;
    }

    public void toggle(){
        if(!enabled){
            enabled = true;
            Commands.get().addEnabled(this);
            onEnable();

        }else{
            enabled = false;
            Commands.get().removeEnabled(this);
            onDisable();
        }
    }

    private void onEnable(){
        PluginCommand command = SwissKnife.INSTANCE.getCommand(name.replaceAll("-", ""));
        if(command != null){
            command.setExecutor(this);
            onRegister();
        }else{
            error("Couldn't find command named " + name + ". This could mean that command is not defined in plugin.yml");
        }
    }

    private void onDisable(){
        PluginCommand command = SwissKnife.INSTANCE.getCommand(name);
        if(command != null){
            command.unregister(Bukkit.getCommandMap()); //TODO: Test
            onUnregister();
        }else{
            error("Couldn't find command named " + name + ". This could mean that command is not defined in plugin.yml");
        }
    }

    public void onRegister(){

    }

    public void onUnregister(){

    }

    public void sendMessage(CommandSender sender, String message){
        sender.sendMessage(Config.get().prefix + " " + message);
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

    private void setCooldown(UUID uuid){
        if(cooldown <= 0){
            return;
        }
        cooldownMap.put(uuid, System.currentTimeMillis());
    }

    public boolean isOnCooldown(UUID uuid){
        if(cooldownMap.get(uuid) == null) return false;
        long timeLeft = System.currentTimeMillis() - cooldownMap.get(uuid);

        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= cooldown;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args){
        if(sender instanceof Player){
            UUID uuid = ((Player) sender).getUniqueId();

            if(isOnCooldown(uuid)){
                sendMessage(sender, ChatColor.RED + "You need to wait before doing this command again");
            }else{
                handleCommand(sender, args);
                setCooldown(uuid);
            }
        }
        return true;
    }

    public abstract void handleCommand(CommandSender sender, String[] args);

    public boolean isEnabled(){
        return enabled;
    }
}
