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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public abstract class Command implements CommandExecutor, TabCompleter {
    public String name;
    private int cooldown = 0;
    private boolean enabled = false;
    private boolean dbDependant = false;


    private final Map<UUID, Long> cooldownMap = new HashMap<>(1);

    public Command(String name){
        this.name = name;
    }
    public Command(String name, boolean dbDependant){
        this.name = name;
        this.dbDependant = dbDependant;
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

    public void register(){
        PluginCommand command = SwissKnife.INSTANCE.getCommand(name.replaceAll("-", ""));
        if(command != null){
            command.setExecutor(this);
            command.setTabCompleter(this);
            onRegister();
        }else{
            error("Couldn't find command named " + name + " while enabling a command. This could mean that command is not defined in plugin.yml");
        }
    }

    private void onEnable(){

    }

    private void onDisable(){

    }

    public void onRegister(){

    }

    public void sendMessage(CommandSender sender, String message){
        if(Config.get().useCommandPrefix){
            if(name.equals("swissknife")){
                sender.sendMessage(Config.get().prefix + " " + message);
            }else{
                sender.sendMessage(Config.get().commandPrefix.replaceAll("%command%", name) + " " + message);
            }
        }else{
            sender.sendMessage(message);
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

    private void setCooldown(UUID uuid){
        if(cooldown <= 0){
            return;
        }
        cooldownMap.put(uuid, System.currentTimeMillis());
    }

    public boolean isOnCooldown(UUID uuid){
        if(cooldownMap.get(uuid) == null) return false;
        long timeLeft = System.currentTimeMillis() - cooldownMap.get(uuid);

        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) < cooldown;
    }

    //TODO: Display time remaining

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args){
        if(!enabled){
            sendMessage(sender, ChatColor.RED + "This command is disabled.");
            return true;
        }
        if(sender instanceof Player){
            UUID uuid = ((Player) sender).getUniqueId();

            if(isOnCooldown(uuid)){
                sendMessage(sender, ChatColor.RED + "You need to wait before doing this command again");
            }else{
                handleCommand(sender, args);
                if(sender.hasPermission("swissknife.bypass.cooldown")){
                    return true;
                }
                setCooldown(uuid);
            }
        }else{
            handleCommand(sender, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, String[] args){
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(@NotNull CommandSender sender, String[] args);

    public abstract void handleCommand(CommandSender sender, String[] args);

    public boolean isEnabled(){
        return enabled;
    }

    public boolean isDbDependant(){
        return dbDependant;
    }
}
